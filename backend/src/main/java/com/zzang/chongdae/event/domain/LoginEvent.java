package com.zzang.chongdae.event.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoginEvent extends ApplicationEvent {

    private final MemberEntity member;

    public LoginEvent(Object source, MemberEntity member) {
        super(source);
        this.member = member;
    }
}
