package com.zzang.chongdae.offering.service.dto;

import java.time.LocalDateTime;

public record OfferingCreateRequest(Long memberId,
                                    String title,
                                    String productUrl,
                                    String thumbnailUrl,
                                    Integer totalCount,
                                    Integer totalPrice,
                                    Integer eachPrice,
                                    String meetingAddress,
                                    String meetingAddressDetail,
                                    LocalDateTime deadline,
                                    String description) {
}
