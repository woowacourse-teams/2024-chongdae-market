package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OfferingMemberFixture {

    @Autowired
    private OfferingMemberRepository offeringMemberRepository;

    @Autowired
    private OfferingRepository offeringRepository;

    public OfferingMemberEntity createProposer(MemberEntity member, OfferingEntity offering) {
        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member,
                offering,
                OfferingMemberRole.PROPOSER
        );
        return offeringMemberRepository.save(offeringMember);
    }

    @Transactional
    public OfferingMemberEntity createParticipant(MemberEntity member, OfferingEntity offering) {
        OfferingMemberEntity offeringMember = new OfferingMemberEntity(
                member,
                offering,
                OfferingMemberRole.PARTICIPANT
        );
        OfferingMemberEntity result = offeringMemberRepository.save(offeringMember);
        offering = offeringRepository.findById(offering.getId()).orElseThrow();
        offering.participate();
        return result;
    }
}
