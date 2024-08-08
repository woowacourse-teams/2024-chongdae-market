package com.zzang.chongdae.offeringmember.domain;

public enum OfferingMemberRole {

    PROPOSER,
    PARTICIPANT; // TODO: entity 패키지와 domain 패키지 중 위치 고민하기 (feat. vo)

    public boolean isProposer() {
        return this == PROPOSER;
    }

    public boolean isParticipant() {
        return this == PARTICIPANT;
    }
}
