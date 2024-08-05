package com.zzang.chongdae.comment.controller;

import com.zzang.chongdae.comment.service.CommentService;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomStatusResponse;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Void> saveComment(
            @RequestBody @Valid CommentSaveRequest request) {
        Long commentId = commentService.saveComment(request);
        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @GetMapping("/comments")
    public ResponseEntity<CommentRoomAllResponse> getAllCommentRoom(
            @RequestParam(value = "member-id") Long loginMemberId) {
        CommentRoomAllResponse response = commentService.getAllCommentRoom(loginMemberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments/{offering-id}")
    public ResponseEntity<CommentAllResponse> getAllComment(
            @PathVariable(value = "offering-id") Long offeringId,
            @RequestParam(value = "member-id") Long loginMemberId) {
        CommentAllResponse response = commentService.getAllComment(offeringId, loginMemberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments/{offering-id}/status")
    public ResponseEntity<CommentRoomStatusResponse> updateCommentRoomStatus(
            @PathVariable(value = "offering-id") Long offeringId,
            @RequestParam(value = "member-id") Long loginMemberId) {
        CommentRoomStatusResponse response = commentService.updateCommentRoomStatus(offeringId, loginMemberId);
        return ResponseEntity.ok(response);
    }
}
