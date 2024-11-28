package com.zzang.chongdae.comment.controller;

import com.zzang.chongdae.comment.service.CommentService;
import com.zzang.chongdae.comment.service.dto.CommentSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment(댓글)")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @PostMapping("/comments")
    public ResponseEntity<Void> saveComment(@RequestBody CommentSaveRequest commentSaveRequest) {
        commentService.saveComment(commentSaveRequest);
        return ResponseEntity.ok().build();
    }
}
