package com.zzang.chongdae.offeringmember.integration;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.zzang.chongdae.global.helper.ConcurrencyExecutor;
import com.zzang.chongdae.global.integration.IntegrationTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.service.dto.ParticipationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;

public class OfferingMemberIntegrationTest extends IntegrationTest {

    @DisplayName("공모 참여")
    @Nested
    class Participate {

        List<FieldDescriptor> requestDescriptors = List.of(
                fieldWithPath("offeringId").description("공모 id (필수)"),
                fieldWithPath("participationCount").description("구매할 물품 개수")
        );
        ResourceSnippetParameters successSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여")
                .description("게시된 공모에 참여합니다.")
                .requestFields(requestDescriptors)
                .requestSchema(schema("ParticipationSuccessRequest"))
                .build();
        ResourceSnippetParameters failSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여")
                .description("게시된 공모에 참여합니다.")
                .requestFields(requestDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("ParticipationFailRequest"))
                .responseSchema(schema("ParticipationSuccessResponse"))
                .build();
        MemberEntity proposer;
        MemberEntity participant;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("dora");
            participant = memberFixture.createMember("poke");
            offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
        }

        @DisplayName("게시된 공모에 참여할 수 있다 - 물품 한개")
        @Test
        void should_participateSuccess() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    1
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("게시된 공모에 참여할 수 있다 - 물품 여러개")
        @Test
        void should_participateSuccess_when_givenMoreThanOne() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    3
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("총대 구매 개수를 요청하지 않은 경우 성공적으로 수행한다 - 이전 버전 고려")
        @Test
        void should_participateSuccess_when_myCountNull() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    null
            );
            RestAssured.given().log().all()
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(201);
        }

        @DisplayName("총 수량을 넘은 개수만큼 요청한 경우 참여할 수 없다.")
        @Test
        void should_throwException_when_givenExceededCount() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    6
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("공모자는 본인이 만든 공모에 참여할 수 없다")
        @Test
        void should_throwException_when_givenProposerParticipate() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    1
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-my-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("유효하지 않은 공모에 참여할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId() + 100,
                    1
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            ParticipationRequest request = new ParticipationRequest(
                    null,
                    1
            );
            RestAssured.given(spec).log().all()
                    .filter(document("participate-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("같은 사용자가 같은 공모에 동시에 참여할 경우 첫 요청 이후의 요청은 예외가 발생한다.")
        @Test
        void should_throwException_when_sameMemberAndSameOffering() throws InterruptedException {
            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    1
            );

            ConcurrencyExecutor concurrencyExecutor = ConcurrencyExecutor.getInstance();
            List<Integer> statusCodes = concurrencyExecutor.execute(5,
                    () -> RestAssured.given().log().all()
                            .cookies(cookieProvider.createCookiesWithMember(participant))
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().post("/participations")
                            .statusCode());

            assertThat(statusCodes).containsExactlyInAnyOrder(201, 400, 400, 400, 400);
        }

        @DisplayName("다른 사용자가 같은 공모에 동시에 참여할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_differentMemberAndSameOffering() throws InterruptedException {
            MemberEntity proposer = memberFixture.createMember("ever");
            OfferingEntity offering = offeringFixture.createOffering(proposer, 2);
            offeringMemberFixture.createProposer(proposer, offering);

            ParticipationRequest request = new ParticipationRequest(
                    offering.getId(),
                    1
            );
            MemberEntity participant1 = memberFixture.createMember("ever1");
            MemberEntity participant2 = memberFixture.createMember("ever2");

            ConcurrencyExecutor concurrencyExecutor = ConcurrencyExecutor.getInstance();
            List<Integer> statusCodes = concurrencyExecutor.execute(
                    () -> RestAssured.given().log().all()
                            .cookies(cookieProvider.createCookiesWithMember(participant1))
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().post("/participations")
                            .statusCode(),
                    () -> RestAssured.given().log().all()
                            .cookies(cookieProvider.createCookiesWithMember(participant2))
                            .contentType(ContentType.JSON)
                            .body(request)
                            .when().post("/participations")
                            .statusCode());

            assertThat(statusCodes).containsExactlyInAnyOrder(201, 400);
        }
    }

    @DisplayName("공모 참여 취소")
    @Nested
    class CancelParticipate {

        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        ResourceSnippetParameters successSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여 취소")
                .description("공모 참여를 취소합니다.")
                .queryParameters(queryParameterDescriptors)
                .requestSchema(schema("CancelParticipateSuccessRequest"))
                .responseSchema(schema("CancelParticipateSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여 취소")
                .description("공모 참여를 취소합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("CancelParticipateFailRequest"))
                .responseSchema(schema("CancelParticipateFailResponse"))
                .build();
        MemberEntity proposer;
        MemberEntity participant;
        OfferingEntity offeringWithGrouping;
        OfferingEntity offeringWithInProgress;
        OfferingEntity offeringNotParticipated;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("dora");
            participant = memberFixture.createMember("poke");

            offeringWithGrouping = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offeringWithGrouping);
            offeringMemberFixture.createParticipant(participant, offeringWithGrouping);

            offeringWithInProgress = offeringFixture.createOffering(proposer, CommentRoomStatus.BUYING);
            offeringMemberFixture.createProposer(proposer, offeringWithInProgress);
            offeringMemberFixture.createParticipant(participant, offeringWithInProgress);

            offeringNotParticipated = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offeringNotParticipated);
        }

        @DisplayName("공모 참여 취소를 할 수 있다.")
        @Test
        void should_cancelSuccess() {
            RestAssured.given(spec).log().all()
                    .filter(document("cancel-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .queryParam("offering-id", offeringWithGrouping.getId())
                    .when().delete("/participations")
                    .then().log().all()
                    .statusCode(204);
        }

        @DisplayName("공모자는 본인이 만든 공모 참여를 취소할 수 없다.")
        @Test
        void should_throwException_when_cancelProposer() {
            RestAssured.given(spec).log().all()
                    .filter(document("cancel-fail-offering-proposer", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .queryParam("offering-id", offeringWithGrouping.getId())
                    .when().delete("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("거래가 진행 중인 공모는 참여를 취소할 수 없다.")
        @Test
        void should_throwException_when_cancelInProgress() {
            RestAssured.given(spec).log().all()
                    .filter(document("cancel-fail-offering-in-progress", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .queryParam("offering-id", offeringWithInProgress.getId())
                    .when().delete("/participations")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("참여하지 않은 공모는 취소할 수 없다.")
        @Test
        void should_throwException_when_cancelNotParticipated() {
            RestAssured.given(spec).log().all()
                    .filter(document("cancel-fail-offering-not-participated", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(participant))
                    .queryParam("offering-id", offeringNotParticipated.getId())
                    .when().delete("/participations")
                    .then().log().all()
                    .statusCode(400);
        }
    }

    @DisplayName("공모 참여자 목록 조회")
    @Nested
    class OfferingParticipants {
        List<ParameterDescriptorWithType> queryParameterDescriptors = List.of(
                parameterWithName("offering-id").description("공모 id (필수)")
        );
        List<FieldDescriptor> successResponseDescriptors = List.of(
                fieldWithPath("proposer.nickname").description("공모 작성자 닉네임"),
                fieldWithPath("proposer.count").description("공모 작성자 참여 개수"),
                fieldWithPath("proposer.price").description("공모 작성자 지불할 금액"),
                fieldWithPath("participants[].nickname").description("공모 참여자 닉네임"),
                fieldWithPath("participants[].count").description("공모 참여자 참여 개수"),
                fieldWithPath("participants[].price").description("공모 참여자 지불할 금액"),
                fieldWithPath("count.currentCount").description("공모 참여자 현재원(작성자 + 참여자)"),
                fieldWithPath("count.totalCount").description("공모 참여자 총원"),
                fieldWithPath("price").description("공모 참여자 예상 정산 가격 - 현재는 불필요하지만 이전 버전 앱을 위해 남겨두었습니다.")
        );
        ResourceSnippetParameters successSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여자 목록 조회")
                .description("공모 참여자들의 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(successResponseDescriptors)
                .requestSchema(schema("OfferingParticipantsSuccessRequest"))
                .responseSchema(schema("OfferingParticipantsSuccessResponse"))
                .build();
        ResourceSnippetParameters failSnippets = ResourceSnippetParameters.builder()
                .summary("공모 참여자 목록 조회")
                .description("공모 참여자들의 목록을 조회합니다.")
                .queryParameters(queryParameterDescriptors)
                .responseFields(failResponseDescriptors)
                .requestSchema(schema("OfferingParticipantsFailRequest"))
                .responseSchema(schema("OfferingParticipantsFailResponse"))
                .build();
        MemberEntity proposer;
        MemberEntity participant;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            proposer = memberFixture.createMember("dora");
            participant = memberFixture.createMember("poke");
            offering = offeringFixture.createOffering(proposer);
            offeringMemberFixture.createProposer(proposer, offering);
            offeringMemberFixture.createParticipant(participant, offering);
        }

        @DisplayName("게시된 공모의 참여자 목록을 확인할 수 있다")
        @Test
        void should_participantsSuccess() {
            RestAssured.given(spec).log().all()
                    .filter(document("participants-success", resource(successSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .queryParam("offering-id", offering.getId())
                    .when().get("/participants")
                    .then().log().all()
                    .statusCode(200);
        }

        @DisplayName("유효하지 않은 공모의 참여자 목록을 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_invalidOffering() {
            RestAssured.given(spec).log().all()
                    .filter(document("participants-fail-invalid-offering", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .queryParam("offering-id", 1000)
                    .when().get("/participants")
                    .then().log().all()
                    .statusCode(400);
        }

        @DisplayName("요청 값에 빈값이 들어오는 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_emptyValue() {
            RestAssured.given(spec).log().all()
                    .filter(document("participants-fail-request-with-null", resource(failSnippets)))
                    .cookies(cookieProvider.createCookiesWithMember(proposer))
                    .when().get("/participants?offering-id=")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
