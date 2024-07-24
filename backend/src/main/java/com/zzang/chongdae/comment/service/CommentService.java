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
import com.zzang.chongdae.comment.service.dto.LatestCommentResponse;
import com.zzang.chongdae.member.repository.MemberRepository;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingWithRole;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import com.zzang.chongdae.offeringmember.domain.OfferingMemberRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final OfferingRepository offeringRepository;

    public void saveComment(CommentSaveRequest request) {
        MemberEntity member = memberRepository.findById(request.memberId())
                .orElseThrow(); // TODO: 예외처리 하기
        OfferingEntity offering = offeringRepository.findById(request.offeringId())
                .orElseThrow();// TODO: 예외처리 하기

        CommentEntity comment = new CommentEntity(member, offering, request.content());
        commentRepository.save(comment);
    }

    public CommentAllResponse getAllComment(Long offeringId, Long memberId) {
        OfferingEntity offering = offeringRepository.findById(offeringId)
                .orElseThrow(); // TODO: 예외 처리하기

        List<CommentWithRole> comments = commentRepository.findAllWithRoleByOffering(offering);
        List<CommentAllResponseItem> responseItems = comments.stream()
                .map(commentWithRole -> {
                    CommentEntity comment = commentWithRole.getComment();
                    OfferingMemberRole role = commentWithRole.getRole();
                    return new CommentAllResponseItem(
                            new CommentCreatedAtResponse(comment.getCreatedAt()),
                            comment.getContent(),
                            comment.getMember().getNickname(),
                            role.isProposer(),
                            comment.getMember().isSameMember(memberId));
                })
                .toList();

        return new CommentAllResponse(responseItems);
    }

    public CommentRoomAllResponse getAllCommentRoom(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(); // TODO: 예외처리 하기

        List<OfferingWithRole> offerings = offeringRepository.findAllByMember(member);
        List<CommentRoomAllResponseItem> responseItems = offerings.stream()
                .map(offering -> commentRepository.findTopByOffering(offering.getOffering())
                        .map(comment -> new CommentRoomAllResponseItem(
                                offering.getOffering().getId(),
                                offering.getOffering().getTitle(),
                                new LatestCommentResponse(comment),
                                offering.getRole().isProposer()
                        ))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .toList();

        return new CommentRoomAllResponse(responseItems);
    }
}
