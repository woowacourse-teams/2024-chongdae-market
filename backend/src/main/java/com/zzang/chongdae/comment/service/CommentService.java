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
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingWithRole;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import java.util.List;
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
                .orElseThrow(); // TODO: 예외처리 하기
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow();// TODO: 예외처리 하기

        CommentEntity comment = new CommentEntity(loginMember, offering, request.content());
        commentRepository.save(comment);
    }

    public CommentRoomAllResponse getAllCommentRoom(Long loginMemberId) {
        MemberEntity loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(); // TODO: 예외처리 하기

        List<OfferingWithRole> offeringsWithRole = offeringRepository.findAllWithRoleByMember(loginMember);
        List<CommentRoomAllResponseItem> responseItems = offeringsWithRole.stream()
                .filter(this::hasComments)
                .map(this::toCommentRoomAllResponseItem)
                .toList();
        return new CommentRoomAllResponse(responseItems);
    }

    private boolean hasComments(OfferingWithRole offeringWithRole) {
        OfferingEntity offering = offeringWithRole.getOffering();
        return commentRepository.existsByOffering(offering);
    }

    private CommentRoomAllResponseItem toCommentRoomAllResponseItem(OfferingWithRole offeringWithRole) {
        OfferingEntity offering = offeringWithRole.getOffering();
        OfferingMemberRole role = offeringWithRole.getRole();
        CommentEntity latestComment = commentRepository.findTopByOfferingOrderByCreatedAtDesc(offering)
                .orElseThrow(); // TODO: 예외처리 하기
        return new CommentRoomAllResponseItem(
                offering.getId(),
                offering.getTitle(),
                new CommentLatestResponse(latestComment),
                role.isProposer());
    }

    public CommentAllResponse getAllComment(Long offeringId, Long loginMemberId) {
        validateMemberExistence(loginMemberId);
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(); // TODO: 예외 처리하기

        List<CommentWithRole> commentsWithRole = commentRepository.findAllWithRoleByOffering(offering);
        List<CommentAllResponseItem> responseItems = commentsWithRole.stream()
                .map(commentWithRole -> toCommentAllResponseItem(commentWithRole, loginMemberId))
                .toList();
        return new CommentAllResponse(responseItems);
    }

    private void validateMemberExistence(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자가 로그인을 했네요"); // TODO: 예외 처리하기
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
