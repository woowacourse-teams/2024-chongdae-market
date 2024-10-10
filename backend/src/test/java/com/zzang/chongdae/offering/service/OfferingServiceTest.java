package com.zzang.chongdae.offering.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingDetailResponse;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import com.zzang.chongdae.offering.service.dto.OfferingUpdateRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OfferingServiceTest extends ServiceTest {

    @Autowired
    OfferingService offeringService;

    @DisplayName("공모 상세 조회")
    @Nested
    class GetOfferingDetail {

        MemberEntity member;
        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            offering = offeringFixture.createOffering(member);
        }

        @DisplayName("공모 id를 통해 공모 상세를 조회할 수 있다")
        @Test
        void should_getOfferingDetail_when_givenOfferingId() {
            // when
            OfferingDetailResponse response = offeringService.getOfferingDetail(offering.getId(), member);

            // then
            assertEquals(offering.getId(), response.id());
        }

        @DisplayName("존재하지 않는 공모 id를 통해 공모 상세를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenInvalidOfferingId() {
            // given
            long invalidOfferingId = offering.getId() + 9999;

            // when & then
            assertThatThrownBy(() -> offeringService.getOfferingDetail(invalidOfferingId, member))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("삭제한 공모 id를 통해 공모 상세를 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenDeletedOfferingId() {
            // given
            offeringFixture.deleteOffering(offering);

            // when & then
            assertThatThrownBy(() -> offeringService.getOfferingDetail(offering.getId(), member))
                    .isInstanceOf(MarketException.class);
        }
    }

    @DisplayName("공모 단건 조회")
    @Nested
    class GetOffering {

        OfferingEntity offering;

        @BeforeEach
        void setUp() {
            MemberEntity member = memberFixture.createMember("ever");
            offering = offeringFixture.createOffering(member);
        }

        @DisplayName("공모 id를 통해 공모를 단건 조회할 수 있다")
        @Test
        void should_getOffering_when_givenOfferingId() {
            // given
            OfferingAllResponseItem expected = new OfferingAllResponseItem(offering, offering.toOfferingPrice());

            // when
            OfferingAllResponseItem actual = offeringService.getOffering(offering.getId());

            // then
            assertEquals(expected, actual);
        }

        @DisplayName("존재하지 않는 공모 id를 통해 공모를 단건 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenInvalidOfferingId() {
            // when & then
            long invalidOfferingId = offering.getId() + 9999;

            assertThatThrownBy(() -> offeringService.getOffering(invalidOfferingId))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("삭제한 공모 id를 통해 공모를 단건 조회할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_givenDeletedOfferingId() {
            // given
            offeringFixture.deleteOffering(offering);

            // when & then
            assertThatThrownBy(() -> offeringService.getOffering(offering.getId()))
                    .isInstanceOf(MarketException.class);
        }
    }

    @DisplayName("공모 목록 조회")
    @Nested
    class GetOfferings {

        MemberEntity member;

        @BeforeEach
        void setUp() {
            member = memberFixture.createMember("dora");
            for (int i = 1; i <= 17; i++) {
                offeringFixture.createOffering(member);
            }
        }

        @DisplayName("최신순으로 공모 목록을 10개씩 조회할 수 있다")
        @Test
        void should_getRecentOfferings() {
            // when
            OfferingAllResponse response = offeringService.getAllOffering("RECENT", null, null, 10);

            // then
            assertEquals(10, response.offerings().size());
        }

        @DisplayName("마지막 페이지 이후로 최신순으로 공모 목록을 조회할 수 있다")
        @Test
        void should_getRecentOfferings_when_givenLastId() {
            // given
            OfferingAllResponse lastResponse = offeringService.getAllOffering("RECENT", null, null, 10);
            List<OfferingAllResponseItem> offerings = lastResponse.offerings();
            Long lastId = offerings.get(offerings.size() - 1).id();

            // when
            OfferingAllResponse response = offeringService.getAllOffering("RECENT", null, lastId, 10);

            // then
            assertEquals(7, response.offerings().size());
        }

        @DisplayName("삭제한 공모는 최신순으로 공모 목록 조회 시 포함되지 않는다")
        @Test
        void should_notIncludeDeletedOffering_when_getOfferings() {
            // given
            offeringFixture.deleteOfferingById(1L);

            // when
            OfferingAllResponse response = offeringService.getAllOffering("RECENT", null, null, 20);

            // then
            assertEquals(16, response.offerings().size());
        }

        @DisplayName("검색어를 지정해 최신순으로 공모 목록을 조회할 수 있다")
        @Test
        void should_getOfferings_when_givenSearchKeyword() {
            // given
            offeringFixture.createOffering(member, "검색어");

            // when
            OfferingAllResponse response = offeringService.getAllOffering("RECENT", "검색", null, 10);

            // then
            assertEquals(1, response.offerings().size());
        }

        @DisplayName("참여 가능한 공모 목록만 조회할 수 있다")
        @Test
        void should_getJoinableOfferings() {
            // when
            OfferingAllResponse response = offeringService.getAllOffering("JOINABLE", null, null, 20);

            // then
            assertEquals(17, response.offerings().size());
        }

        @DisplayName("마지막 페이지 이후로 참여 가능한 공모 목록을 조회할 수 있다")
        @Test
        void should_getJoinableOfferings_when_givenLastId() {
            // given
            OfferingAllResponse lastResponse = offeringService.getAllOffering("JOINABLE", null, null, 10);
            List<OfferingAllResponseItem> offerings = lastResponse.offerings();
            Long lastId = offerings.get(offerings.size() - 1).id();

            // when
            OfferingAllResponse response = offeringService.getAllOffering("JOINABLE", null, lastId, 10);

            // then
            assertEquals(7, response.offerings().size());
        }

        @DisplayName("삭제한 공모는 참여 가능한 공모 목록 조회 시 포함되지 않는다")
        @Test
        void should_notIncludeDeletedOffering_when_getJoinableOfferings() {
            // given
            offeringFixture.deleteOfferingById(1L);

            // when
            OfferingAllResponse response = offeringService.getAllOffering("JOINABLE", null, null, 20);

            // then
            assertEquals(16, response.offerings().size());
        }

        @DisplayName("마감 임박한 공모 목록만 조회할 수 있다")
        @Test
        void should_getImminentOfferings() {
            // when
            offeringFixture.createOffering(member, OfferingStatus.IMMINENT);
            OfferingAllResponse response = offeringService.getAllOffering("IMMINENT", null, null, 20);

            // then
            assertEquals(1, response.offerings().size());
        }

        @DisplayName("마지막 페이지 이후로 마감 임박한 공모 목록을 조회할 수 있다")
        @Test
        void should_getImminentOfferings_when_givenLastId() {
            // given
            offeringFixture.createOffering(member, OfferingStatus.IMMINENT);
            offeringFixture.createOffering(member, OfferingStatus.IMMINENT);
            OfferingAllResponse lastResponse = offeringService.getAllOffering("IMMINENT", null, null, 1);
            List<OfferingAllResponseItem> offerings = lastResponse.offerings();
            Long lastId = offerings.get(offerings.size() - 1).id();

            // when
            OfferingAllResponse response = offeringService.getAllOffering("IMMINENT", null, lastId, 1);

            // then
            assertEquals(1, response.offerings().size());
        }

        @DisplayName("삭제한 공모는 마감 임박한 공모 목록 조회 시 포함되지 않는다")
        @Test
        void should_notIncludeDeletedOffering_when_getImminentOfferings() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(member, OfferingStatus.IMMINENT);
            offeringFixture.createOffering(member, OfferingStatus.IMMINENT);
            offeringFixture.deleteOffering(offering);

            // when
            OfferingAllResponse response = offeringService.getAllOffering("IMMINENT", null, null, 20);

            // then
            assertEquals(1, response.offerings().size());
        }

        @DisplayName("높은 할인율 순으로 공모 목록을 조회할 수 있다")
        @Test
        void should_getHighDiscountOfferings() {
            // given
            offeringFixture.createOffering(member, 50.0);
            offeringFixture.createOffering(member, 40.0);

            // when
            OfferingAllResponse response = offeringService.getAllOffering("HIGH_DISCOUNT", null, null, 20);

            // then
            assertEquals(50.0, response.offerings().get(0).discountRate());
            assertEquals(40.0, response.offerings().get(1).discountRate());
            assertEquals(33.3, response.offerings().get(2).discountRate());
        }

        @DisplayName("마지막 페이지 이후로 높은 할인율 순으로 공모 목록을 조회할 수 있다")
        @Test
        void should_getHighDiscountOfferings_when_givenLastDiscountRate() {
            // given
            offeringFixture.createOffering(member, 50.0);
            offeringFixture.createOffering(member, 40.0);
            OfferingAllResponse lastResponse = offeringService.getAllOffering("HIGH_DISCOUNT", null, null, 1);
            List<OfferingAllResponseItem> offerings = lastResponse.offerings();
            Long lastId = offerings.get(offerings.size() - 1).id();

            // when
            OfferingAllResponse response = offeringService.getAllOffering("HIGH_DISCOUNT", null, lastId, 10);

            // then
            assertEquals(40.0, response.offerings().get(0).discountRate());
            assertEquals(33.3, response.offerings().get(1).discountRate());
        }

        @DisplayName("삭제한 공모는 높은 할인율 순으로 공모 목록 조회 시 포함되지 않는다")
        @Test
        void should_notIncludeDeletedOffering_when_getHighDiscountOfferings() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(member, 50.0);
            offeringFixture.createOffering(member, 40.0);
            offeringFixture.deleteOffering(offering);

            // when
            OfferingAllResponse response = offeringService.getAllOffering("HIGH_DISCOUNT", null, null, 20);

            // then
            assertEquals(40.0, response.offerings().get(0).discountRate());
        }
    }

    @DisplayName("공모 작성")
    @Nested
    class CreateOffering {

        @DisplayName("공목 등록 시 원 가격 정보가 없더라도 공모 작성에 성공할 수 있다.")
        @Test
        void should_createOffering_when_givenOfferingWithoutOriginPriceCreateRequest() {
            // given
            MemberEntity member = memberFixture.createMember("pizza");
            OfferingSaveRequest request = new OfferingSaveRequest(
                    "공모 제목",
                    "www.naver.com",
                    "www.naver.com/favicon.ico",
                    5,
                    10000,
                    null,
                    "서울특별시 광진구 구의강변로 3길 11",
                    "상세주소아파트",
                    "구의동",
                    LocalDateTime.parse("2030-10-11T10:00:00"),
                    "내용입니다."
            );
            Long expected = 1L;

            // when
            Long actual = offeringService.saveOffering(request, member);

            // then
            assertEquals(expected, actual);
        }
    }

    @DisplayName("공모 수정")
    @Nested
    class UpdateOffering {

        @DisplayName("공모를 수정할 수 있음")
        @Test
        void should_updateOffering_when_givenOfferingIdAndOfferingUpdateRequest() {
            // given
            MemberEntity member = memberFixture.createMember("poke");
            OfferingEntity offering = offeringFixture.createOffering(member);
            String expected = "수정된 공모 제목";
            OfferingUpdateRequest request = new OfferingUpdateRequest(
                    expected,
                    "https://to.be.updated/productUrl",
                    "https://to.be.updated/thumbnail/url",
                    10,
                    20000,
                    5000,
                    "수정할 모집 장소 주소",
                    "수정할 모집 상세 주소",
                    "수정된동",
                    LocalDateTime.parse("2024-12-31T00:00:00"),
                    "수정할 공모 상세 내용"
            );

            // when
            offeringService.updateOffering(offering.getId(), request, member);
            OfferingAllResponseItem modifiedOffering = offeringService.getOffering(offering.getId());
            String actual = modifiedOffering.title();

            // then
            assertEquals(expected, actual);
        }
    }

    @DisplayName("공모 삭제")
    @Nested
    class DeleteOffering {

        MemberEntity notProposer;
        MemberEntity proposer;

        @BeforeEach
        void setUp() {
            notProposer = memberFixture.createMember("never");
            proposer = memberFixture.createMember("ever");
        }

        @DisplayName("공모 id와 총대 엔티티가 주어졌을 때 공모를 삭제할 수 있다.")
        @Test
        void should_deleteOfferingSoftly_when_givenOfferingIdAndMember() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(proposer);

            // when
            offeringService.deleteOffering(offering.getId(), proposer);

            // then
            assertThat(offeringFixture.countOffering()).isEqualTo(0);
        }

        @DisplayName("총대가 아닌 사용자가 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_deleteWithNotProposer() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(proposer);

            // when & then
            assertThatThrownBy(() -> offeringService.deleteOffering(offering.getId(), notProposer))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("유효하지 않은 공모 id에 대해 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_deleteWithInvalidOfferingId() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(proposer);

            // when & then
            long invalidOfferingId = offering.getId() + 9999;

            assertThatThrownBy(() -> offeringService.deleteOffering(invalidOfferingId, proposer))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("거래 인원이 확정되고 거래가 완료되기 전 (구매 중 상태) 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_deleteAtBuyingStatus() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(proposer, CommentRoomStatus.BUYING);

            // when & then
            assertThatThrownBy(() -> offeringService.deleteOffering(offering.getId(), proposer))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("거래 인원이 확정되고 거래가 완료되기 전 (거래 중 상태) 삭제를 시도할 경우 예외가 발생한다.")
        @Test
        void should_throwException_when_deleteAtTradingStatus() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(proposer, CommentRoomStatus.TRADING);

            // when & then
            assertThatThrownBy(() -> offeringService.deleteOffering(offering.getId(), proposer))
                    .isInstanceOf(MarketException.class);
        }

        @DisplayName("거래 완료 상태 공모의 경우 삭제가 가능하다.")
        @Test
        void should_deleteAvailable_when_statusDone() {
            // given
            OfferingEntity offering = offeringFixture.createOffering(proposer, CommentRoomStatus.DONE);

            // when
            offeringService.deleteOffering(offering.getId(), proposer);

            // then
            assertThat(offeringFixture.countOffering()).isEqualTo(0);
        }
    }
}
