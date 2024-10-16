package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

import com.zzang.chongdae.domain.model.Comment

sealed class CommentViewType {
    data class MyComment(val comment: Comment) : CommentViewType()

    data class OtherComment(val comment: Comment) : CommentViewType()

    companion object {
        fun fromComment(comment: Comment): CommentViewType {
            return if (comment.isMine) {
                MyComment(comment)
            } else {
                OtherComment(comment)
            }
        }
    }
}
