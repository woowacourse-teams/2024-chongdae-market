package com.zzang.chongdae.offering.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingCondition;
import com.zzang.chongdae.offering.domain.OfferingMeeting;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    private static final int INITIAL_COUNT = 1;

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
    private LocalDateTime meetingDate;

    @NotNull
    private String meetingAddress;

    private String meetingAddressDetail;

    private String meetingAddressDong;

    @NotNull
    @Positive
    private Integer totalCount;

    @NotNull
    @Positive
    private Integer currentCount = INITIAL_COUNT;

    @NotNull
    private Boolean isManualConfirmed; // TODO: remove

    @NotNull
    @Positive
    private Integer totalPrice;

    @Positive
    private Integer originPrice;

    @Positive
    private Double discountRate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OfferingStatus offeringStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommentRoomStatus roomStatus;

    public OfferingEntity(MemberEntity member, String title, String description, String thumbnailUrl, String productUrl,
                          LocalDateTime meetingDate, String meetingAddress, String meetingAddressDetail,
                          String meetingAddressDong,
                          Integer totalCount, Integer currentCount, Boolean isManualConfirmed, Integer totalPrice,
                          Integer originPrice, Double discountRate,
                          OfferingStatus offeringStatus, CommentRoomStatus roomStatus) {
        this(null, member, title, description, thumbnailUrl, productUrl, meetingDate, meetingAddress,
                meetingAddressDetail, meetingAddressDong, totalCount, currentCount, isManualConfirmed, totalPrice,
                originPrice, discountRate, offeringStatus, roomStatus);
    }

    public void participate() {
        currentCount++;
    }

    public void leave() {
        currentCount--;
    }

    public CommentRoomStatus moveCommentRoomStatus() {
        this.roomStatus = roomStatus.nextStatus();
        return this.roomStatus;
    }

    public void manuallyConfirm() {
        this.isManualConfirmed = true;
    }

    public OfferingPrice toOfferingPrice() {
        return new OfferingPrice(totalCount, totalPrice, originPrice);
    }

    public OfferingCondition toOfferingCondition() {
        return new OfferingCondition(meetingDate, totalCount, isManualConfirmed, currentCount);
    }

    public OfferingMeeting toOfferingMeeting() {
        return new OfferingMeeting(meetingDate, meetingAddress, meetingAddressDetail, meetingAddressDong);
    }

    public boolean isProposedBy(MemberEntity other) {
        return this.member.isSame(other);
    }

    public boolean isNotProposedBy(MemberEntity other) {
        return !isProposedBy(other);
    }

    public void updateMeeting(OfferingMeeting offeringMeeting) {
        this.meetingDate = offeringMeeting.getMeetingDate();
        this.meetingAddress = offeringMeeting.getMeetingAddress();
        this.meetingAddressDetail = offeringMeeting.getMeetingAddressDetail();
        this.meetingAddressDong = offeringMeeting.getMeetingAddressDong();
    }

    public void updateOfferingStatus(OfferingStatus offeringStatus) {
        this.offeringStatus = offeringStatus;
    }

    public void updateRoomStatus(CommentRoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }
}
