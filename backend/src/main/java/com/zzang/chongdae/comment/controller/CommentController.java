package com.zzang.chongdae.comment.controller;

import com.zzang.chongdae.comment.service.CommentService;
import com.zzang.chongdae.comment.service.dto.CommentAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentRoomAllResponse;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment(댓글)")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @PostMapping("/comments")
    public ResponseEntity<Void> saveComment(
            @RequestBody CommentSaveRequest commentSaveRequest) {
        commentService.saveComment(commentSaveRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글방 목록 조회", description = "댓글방 목록을 조회합니다.")
    @GetMapping("/comments")
    public ResponseEntity<CommentRoomAllResponse> getAllCommentRoom(
            @RequestParam(value = "member-id") Long loginMemberId) {
        CommentRoomAllResponse response = commentService.getAllCommentRoom(loginMemberId);
        return ResponseEntity.ok(response); // TODO: 총대 한명만 존재하는 댓글방도 댓글방 목록 조회 시 띄울 것인지
    }

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회합니다.")
    @GetMapping("/comments/{offering-id}")
    public ResponseEntity<CommentAllResponse> getAllComment(
            @PathVariable(value = "offering-id") Long offeringId,
            @RequestParam(value = "member-id") Long loginMemberId) {
        CommentAllResponse response = commentService.getAllComment(offeringId, loginMemberId);
        return ResponseEntity.ok(response);
    }
}
