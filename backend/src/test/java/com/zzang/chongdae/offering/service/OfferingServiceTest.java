package com.zzang.chongdae.offering.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OfferingServiceTest extends ServiceTest {

    @Autowired
    OfferingService offeringService;

    @DisplayName("공모 id를 통해 공모를 단건 조회할 수 있다.")
    @Test
    void should_getOffering_when_givenOfferingId() {
        // given
        MemberEntity member = memberFixture.createMember("ever");
        OfferingEntity offering = offeringFixture.createOffering(member);
        OfferingAllResponseItem expected = new OfferingAllResponseItem(offering, offering.toOfferingPrice());

        // when
        OfferingAllResponseItem actual = offeringService.getOffering(offering.getId());

        // then
        assertEquals(expected, actual);
    }

    @DisplayName("유효하지 않은 공모 id를 통해 공모를 단건 조회할 경우 예외가 발생한다.")
    @Test
    void should_throwException_when_givenInvalidOfferingId() {
        // given
        MemberEntity member = memberFixture.createMember("ever");
        OfferingEntity offering = offeringFixture.createOffering(member);

        // when & then
        long invalidOfferingId = offering.getId() + 9999;

        assertThatThrownBy(() -> offeringService.getOffering(invalidOfferingId))
                .isInstanceOf(MarketException.class);
    }

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
                LocalDateTime.parse("2024-10-11T10:00:00"),
                "내용입니다."
        );
        Long expected = 1L;

        // when
        Long actual = offeringService.saveOffering(request, member);

        // then
        assertEquals(expected, actual);
    }
}
