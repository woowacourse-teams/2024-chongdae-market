package com.zzang.chongdae.notification.domain.notification;

import com.google.firebase.messaging.MulticastMessage;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmTokens;
import com.zzang.chongdae.notification.service.FcmMessageManager;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OfferingNotification {

    private static final String MESSAGE_TITLE = "새로운 공모를 확인해보세요!";
    private static final String MESSAGE_TYPE = "offering_detail";

    private final FcmMessageManager messageManager;
    private final OfferingEntity offering;
    private final List<MemberEntity> members;

    @Nullable
    public MulticastMessage messageWhenSaveOffering() {
        FcmTokens tokens = FcmTokens.from(membersNotWriter());
        if (tokens.isEmpty()) {
            return null;
        }
        FcmData data = new FcmData();
        data.addData("title", MESSAGE_TITLE);
        data.addData("body", offering.getTitle());
        data.addData("offering_id", offering.getId());
        data.addData("type", MESSAGE_TYPE);
        return messageManager.createMessages(tokens, data);
    }

    private List<MemberEntity> membersNotWriter() {
        return members.stream()
                .filter(member -> !member.isSame(offering.getMember()))
                .toList(); // TODO: filter 성능 확인 후 찾아서 Remove 하는 방향과 성능 비교
    }
}
