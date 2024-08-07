package com.zzang.chongdae.offeringmember.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MemberEntity member;

    @ManyToOne
    private OfferingEntity offering;

    @Enumerated(EnumType.STRING)
    private OfferingMemberRole role;

    public OfferingMemberEntity(MemberEntity member, OfferingEntity offering, OfferingMemberRole role) {
        this(null, member, offering, role);
    }

    public boolean isSameMember(MemberEntity member) {
        return this.member.getId().equals(member.getId());
    }

    public boolean isProposer() {
        return this.role.isProposer();
    }
}
