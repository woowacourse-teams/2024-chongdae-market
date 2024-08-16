package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.service.dto.OfferingSaveRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OfferingServiceTest extends ServiceTest {

    @Autowired
    OfferingService offeringService;

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
        Assertions.assertEquals(expected, actual);
    }
}
