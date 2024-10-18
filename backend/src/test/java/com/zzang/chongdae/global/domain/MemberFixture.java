package com.zzang.chongdae.global.domain;

import com.zzang.chongdae.member.domain.AuthProvider;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberFixture {

    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity createMember(String nickname) {
        return createMember(nickname, "fcmToken_" + nickname);
    }
    
    public MemberEntity createMember(String nickname, String fcmToken) {
        MemberEntity member = new MemberEntity(
                nickname,
                AuthProvider.KAKAO,
                AuthProvider.KAKAO.buildLoginId(nickname),
                "1234",
                fcmToken);
        return memberRepository.save(member);
    }

    public List<MemberEntity> createMembers(int memberCount) {
        List<MemberEntity> members = new ArrayList<>();
        for (int i = 0; i < memberCount; i++) {
            MemberEntity member = createMember("user_%d".formatted(i));
            members.add(member);
        }
        return Collections.unmodifiableList(members);
    }
}
