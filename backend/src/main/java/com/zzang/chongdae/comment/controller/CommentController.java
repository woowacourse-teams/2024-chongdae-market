package com.zzang.chongdae.comment.controller;

import com.zzang.chongdae.comment.domain.SearchDirection;
import com.zzang.chongdae.comment.service.CommentService;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomInfoResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomStatusResponse;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import com.zzang.chongdae.logging.config.LoggingMasked;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 30;

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Void> saveComment(
            @RequestBody @Valid CommentSaveRequest request,
            MemberEntity member) {
        Long commentId = commentService.saveComment(request, member);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @GetMapping("/comments")
    public ResponseEntity<CommentRoomAllResponse> getAllCommentRoom(
            MemberEntity member) {
        CommentRoomAllResponse response = commentService.getAllCommentRoom(member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments/info")
    public ResponseEntity<CommentRoomInfoResponse> getCommentRoomInfo(
            @RequestParam(value = "offering-id") Long offeringId,
            MemberEntity member) {
        CommentRoomInfoResponse response = commentService.getCommentRoomInfo(offeringId, member);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/comments/status")
    public ResponseEntity<CommentRoomStatusResponse> updateCommentRoomStatus(
            @RequestParam(value = "offering-id") Long offeringId,
            MemberEntity member) {
        CommentRoomStatusResponse response = commentService.updateCommentRoomStatus(offeringId, member);
        return ResponseEntity.ok(response);
    }

    @LoggingMasked
    @GetMapping("/comments/messages")
    public ResponseEntity<CommentAllResponse> getAllComment(
            @RequestParam(value = "offering-id") Long offeringId,
            @RequestParam(value = "direction", required = false, defaultValue = "PREVIOUS") SearchDirection direction,
            @RequestParam(value = "last-id", required = false) Long lastId,
            @RequestParam(value = "page-size", defaultValue = "30") @Min(MIN_PAGE_SIZE) @Max(MAX_PAGE_SIZE) Integer pageSize,
            MemberEntity member) {
        CommentAllResponse response = commentService.getAllComment(offeringId, member, direction, lastId, pageSize);
        return ResponseEntity.ok(response);
    }
}
