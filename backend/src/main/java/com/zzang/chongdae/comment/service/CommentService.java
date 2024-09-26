package com.zzang.chongdae.comment.service;

import com.zzang.chongdae.comment.exception.CommentErrorCode;
import com.zzang.chongdae.comment.repository.CommentRepository;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentLatestResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponseItem;
import com.zzang.chongdae.comment.service.dto.CommentRoomInfoResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomStatusResponse;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.exception.OfferingMemberErrorCode;
import com.zzang.chongdae.offeringmember.repository.OfferingMemberRepository;
import com.zzang.chongdae.offeringmember.repository.entity.OfferingMemberEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final OfferingRepository offeringRepository;
    private final OfferingMemberRepository offeringMemberRepository;

    public Long saveComment(CommentSaveRequest request, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsJoined(member, offering);
        CommentEntity comment = new CommentEntity(member, offering, request.content());
        CommentEntity savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    private void validateIsJoined(MemberEntity member, OfferingEntity offering) {
        if (!offeringMemberRepository.existsByOfferingAndMember(offering, member)) {
            throw new MarketException(OfferingMemberErrorCode.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public CommentRoomAllResponse getAllCommentRoom(MemberEntity member) {
        List<Long> offeringIds = offeringMemberRepository.findOfferingIdsByMember(member.getId());
        List<CommentRoomAllResponseItem> responseItems = offeringIds.stream()
                .map(offeringId -> getCommentRoom(offeringId, member))
                .toList();
        return new CommentRoomAllResponse(responseItems);
    }

    private CommentRoomAllResponseItem getCommentRoom(Long offeringId, MemberEntity member) {
        OfferingMemberEntity offeringMember = offeringMemberRepository.findByOfferingIdAndMember(offeringId, member)
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.NOT_FOUND));
        CommentLatestResponse latestComment = getLatestComment(offeringId);
        if (offeringRepository.existsById(offeringId)) {
            return new CommentRoomAllResponseItem(offeringMember, latestComment);
        }
        return new CommentRoomAllResponseItem(offeringId, offeringMember, latestComment);
    }

    private CommentLatestResponse getLatestComment(Long offeringId) {
        Optional<CommentEntity> comment = commentRepository.findTopByOfferingIdOrderByCreatedAtDesc(offeringId);
        return comment.map(CommentLatestResponse::new)
                .orElseGet(() -> new CommentLatestResponse(null, null));
    }

    public CommentRoomInfoResponse getCommentRoomInfo(Long offeringId, MemberEntity member) {
        if (offeringRepository.existsById(offeringId)) {
            return getExistedCommentRoomInfo(offeringId, member);
        }
        return getDeletedCommentRoomInfo(offeringId, member);
    }

    private CommentRoomInfoResponse getExistedCommentRoomInfo(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsJoined(member, offering);
        return new CommentRoomInfoResponse(offering, member);
    }

    private CommentRoomInfoResponse getDeletedCommentRoomInfo(Long offeringId, MemberEntity member) {
        OfferingMemberEntity offeringMember = offeringMemberRepository.findByOfferingIdAndMember(offeringId, member)
                .orElseThrow(() -> new MarketException(OfferingMemberErrorCode.NOT_FOUND));
        boolean isProposer = offeringMember.isProposer(); // TODO : 도메인으로 정리
        return new CommentRoomInfoResponse(isProposer);
    }

    @Transactional
    public CommentRoomStatusResponse updateCommentRoomStatus(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsProposer(member, offering);
        CommentRoomStatus updatedStatus = offering.moveCommentRoomStatus();
        if (updatedStatus.isBuying()) { // TODO : 도메인으로 정리
            offering.updateOfferingStatus(OfferingStatus.CONFIRMED);
        }
        return new CommentRoomStatusResponse(updatedStatus);
    }

    private void validateIsProposer(MemberEntity member, OfferingEntity offering) {
        if (offering.isNotProposedBy(member)) {
            throw new MarketException(CommentErrorCode.NOT_PROPOSER);
        }
    }

    public CommentAllResponse getAllComment(Long offeringId, MemberEntity member) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND));
        validateIsJoined(member, offering);
        List<CommentEntity> comments = commentRepository.findAllByOfferingOrderByCreatedAt(offering);
        List<CommentAllResponseItem> responseItems = comments.stream()
                .map(comment -> new CommentAllResponseItem(comment, member))
                .toList();
        return new CommentAllResponse(responseItems);
    }
}
