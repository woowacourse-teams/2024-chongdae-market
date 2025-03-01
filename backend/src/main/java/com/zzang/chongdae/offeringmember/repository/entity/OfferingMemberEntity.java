package com.zzang.chongdae.offeringmember.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "offering_member")
@Entity
public class OfferingMemberEntity extends BaseTimeEntity {

    private static final int DEFAULT_PARTICIPATION_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private OfferingEntity offering;

    @Enumerated(EnumType.STRING)
    private OfferingMemberRole role;

    @NotNull
    @Positive
    private Integer participationCount = DEFAULT_PARTICIPATION_COUNT;

    public OfferingMemberEntity(MemberEntity member, OfferingEntity offering, OfferingMemberRole role, Integer count) {
        this(null, member, offering, role, Objects.requireNonNullElse(count, DEFAULT_PARTICIPATION_COUNT));
    }

    public boolean isProposer() {
        return this.role.isProposer();
    }

    public boolean isParticipant() {
        return this.role.isParticipant();
    }
}
