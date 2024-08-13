package com.zzang.chongdae.comment.service;

import com.zzang.chongdae.comment.domain.CommentWithRole;
import com.zzang.chongdae.comment.repository.CommentRepository;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentCreatedAtResponse;
import com.zzang.chongdae.comment.service.dto.CommentLatestResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
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
    private final OfferingRepository offeringRepository;

    public Long saveComment(CommentSaveRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));

        CommentEntity comment = new CommentEntity(member, offering, request.content());
        CommentEntity savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    public CommentRoomAllResponse getAllCommentRoom(MemberEntity member) {
        List<OfferingEntity> commentRooms = offeringRepository.findCommentRoomsByMember(member);
        List<CommentRoomAllResponseItem> responseItems = commentRooms.stream()
                .map(commentsRoom -> toCommentRoomAllResponseItem(commentsRoom, member))
                .toList();
        return new CommentRoomAllResponse(responseItems);
    }

    private CommentRoomAllResponseItem toCommentRoomAllResponseItem(OfferingEntity offering, MemberEntity member) {
        return new CommentRoomAllResponseItem(
                offering.getId(),
                offering.getTitle(),
                offering.isProposedBy(member),
                toCommentLatestResponse(offering));
    }

    private CommentLatestResponse toCommentLatestResponse(OfferingEntity offering) {
        Optional<CommentEntity> rawComment = commentRepository.findTopByOfferingOrderByCreatedAtDesc(offering);
        return rawComment
                .map(CommentLatestResponse::new)
                .orElseGet(() -> new CommentLatestResponse(null, null));
    }

    public CommentAllResponse getAllComment(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));

        List<CommentWithRole> commentsWithRole = commentRepository.findAllWithRoleByOffering(offering);
        List<CommentAllResponseItem> responseItems = commentsWithRole.stream()
                .map(commentWithRole -> toCommentAllResponseItem(commentWithRole, member.getId()))
                .toList();
        return new CommentAllResponse(responseItems);
    }

    private CommentAllResponseItem toCommentAllResponseItem(CommentWithRole commentWithRole, long loginMemberId) {
        CommentEntity comment = commentWithRole.getComment();
        OfferingMemberRole role = commentWithRole.getRole();
        MemberEntity member = comment.getMember();
        return new CommentAllResponseItem(
                comment.getId(),
                new CommentCreatedAtResponse(comment.getCreatedAt()),
                comment.getContent(),
                member.getNickname(),
                role.isProposer(),
                member.isSameMember(loginMemberId));
    }
}
