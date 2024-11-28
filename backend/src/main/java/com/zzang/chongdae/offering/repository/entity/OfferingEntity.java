package com.zzang.chongdae.offering.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingMeeting;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "offering")
@Entity
public class OfferingEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MemberEntity member;

    @NotNull
    @Column(length = 30)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String productUrl;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private String meetingAddress;

    private String meetingAddressDetail;

    @NotNull
    @Positive
    private Integer totalCount;

    @NotNull
    @Positive
    private Integer currentCount = 1;

    @NotNull
    private Boolean isManualConfirmed;

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    public OfferingEntity(MemberEntity member, String title, String description, String thumbnailUrl, String productUrl,
                          LocalDateTime deadline, String meetingAddress, String meetingAddressDetail,
                          Integer totalCount, Integer currentCount, Boolean isManualConfirmed, BigDecimal totalPrice) {
        this(null, member, title, description, thumbnailUrl, productUrl, deadline, meetingAddress,
                meetingAddressDetail, totalCount, currentCount, isManualConfirmed, totalPrice);
    }

    public void updateCurrentCount() {
        currentCount++;
    }

    public OfferingPrice toOfferingPrice() {
        return new OfferingPrice(totalCount, totalPrice);
    }

    public OfferingStatus toOfferingStatus() {
        return new OfferingStatus(deadline, totalCount, isManualConfirmed, currentCount);
    }

    public OfferingMeeting toOfferingMeeting() {
        return new OfferingMeeting(deadline, meetingAddress, meetingAddressDetail);
    }
}
