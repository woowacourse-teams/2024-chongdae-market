package com.zzang.chongdae.comment.controller;

import com.zzang.chongdae.comment.service.CommentService;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

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

    @GetMapping("/comments/{offering-id}")
    public ResponseEntity<CommentAllResponse> getAllComment(
            @PathVariable(value = "offering-id") Long offeringId,
            MemberEntity member) {
        CommentAllResponse response = commentService.getAllComment(offeringId, member);
        return ResponseEntity.ok(response);
    }
}
