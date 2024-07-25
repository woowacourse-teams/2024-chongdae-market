package com.zzang.chongdae.comment.service;

import com.zzang.chongdae.comment.domain.CommentWithRole;
import com.zzang.chongdae.comment.repository.CommentRepository;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentCreatedAtResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import com.zzang.chongdae.comment.service.dto.CommentLatestResponse;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.exception.MemberErrorCode;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingWithRole;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final OfferingRepository offeringRepository;

    public void saveComment(CommentSaveRequest request) {
        MemberEntity loginMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));

        CommentEntity comment = new CommentEntity(loginMember, offering, request.content());
        commentRepository.save(comment);
    }

    public CommentRoomAllResponse getAllCommentRoom(Long loginMemberId) {
        MemberEntity loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MarketException(MemberErrorCode.NOT_FOUND));

        List<OfferingWithRole> offeringsWithRole = offeringRepository.findAllWithRoleByMember(loginMember);
        List<CommentRoomAllResponseItem> responseItems = offeringsWithRole.stream()
                .filter(offeringWithRole -> offeringWithRole.getOffering().hasParticipant())
                .map(this::toCommentRoomAllResponseItem)
                .toList();
        return new CommentRoomAllResponse(responseItems);
    }

    private CommentRoomAllResponseItem toCommentRoomAllResponseItem(OfferingWithRole offeringWithRole) {
        OfferingEntity offering = offeringWithRole.getOffering();
        OfferingMemberRole role = offeringWithRole.getRole();
        return new CommentRoomAllResponseItem(
                offering.getId(),
                offering.getTitle(),
                toCommentLatestResponse(offering),
                role.isProposer());
    }

    private CommentLatestResponse toCommentLatestResponse(OfferingEntity offering) {
        Optional<CommentEntity> rawComment = commentRepository.findTopByOfferingOrderByCreatedAtDesc(offering);
        return rawComment
                .map(CommentLatestResponse::new)
                .orElseGet(() -> new CommentLatestResponse(null, null));
    }

    public CommentAllResponse getAllComment(Long offeringId, Long loginMemberId) {
        validateMemberExistence(loginMemberId);
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));

        List<CommentWithRole> commentsWithRole = commentRepository.findAllWithRoleByOffering(offering);
        List<CommentAllResponseItem> responseItems = commentsWithRole.stream()
                .map(commentWithRole -> toCommentAllResponseItem(commentWithRole, loginMemberId))
                .toList();
        return new CommentAllResponse(responseItems);
    }

    private void validateMemberExistence(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MarketException(MemberErrorCode.NOT_FOUND);
        }
    }

    private CommentAllResponseItem toCommentAllResponseItem(CommentWithRole commentWithRole, long loginMemberId) {
        CommentEntity comment = commentWithRole.getComment();
        OfferingMemberRole role = commentWithRole.getRole();
        MemberEntity member = comment.getMember();
        return new CommentAllResponseItem(
                new CommentCreatedAtResponse(comment.getCreatedAt()),
                comment.getContent(),
                member.getNickname(),
                role.isProposer(),
                member.isSameMember(loginMemberId));
    }
}
