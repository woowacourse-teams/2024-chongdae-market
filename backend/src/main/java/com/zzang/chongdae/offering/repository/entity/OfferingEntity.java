package com.zzang.chongdae.offering.repository.entity;

import com.zzang.chongdae.common.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
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
    private Integer totalCount;

    @NotNull
    private Boolean isManualConfirmed;

    @NotNull
    private BigDecimal totalPrice;

    public OfferingPrice toOfferingPrice() {
        return new OfferingPrice(totalCount, totalPrice);
    }

    public OfferingStatus toOfferingStatus(int currentCount) {
        return new OfferingStatus(deadline, totalCount, isManualConfirmed, currentCount);
    }
}
