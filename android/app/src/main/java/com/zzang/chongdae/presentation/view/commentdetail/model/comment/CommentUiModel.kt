package com.zzang.chongdae.presentation.view.commentdetail.model.comment

import com.zzang.chongdae.domain.model.comment.Comment
import com.zzang.chongdae.presentation.util.toFormattedDate
import com.zzang.chongdae.presentation.util.toFormattedTime
import com.zzang.chongdae.presentation.view.commentdetail.adapter.comment.CommentViewType

data class CommentUiModel(
    val content: String,
    val date: String,
    val time: String,
    val isMine: Boolean,
    val isProposer: Boolean,
    val nickname: String,
    val commentViewType: CommentViewType,
) {
    companion object {
        fun Comment.toUiModel(): CommentUiModel {
            val viewType = if (this.isMine) CommentViewType.MyComment else CommentViewType.OtherComment
            return CommentUiModel(
                content = this.content,
                date = this.commentCreatedAt.date.toFormattedDate(),
                time = this.commentCreatedAt.time.toFormattedTime(),
                isMine = this.isMine,
                isProposer = this.isProposer,
                nickname = this.nickname,
                commentViewType = viewType,
            )
        }

        fun createDateSeparator(date: String): CommentUiModel {
            return CommentUiModel(
                content = "",
                date = date,
                time = "",
                isMine = false,
                isProposer = false,
                nickname = "",
                commentViewType = CommentViewType.DateSeparator,
            )
        }

        fun List<Comment>.toUiModelListWithSeparators(): List<CommentUiModel> {
            val uiModels = mutableListOf<CommentUiModel>()
            var currentDate: String? = null

            for (comment in this) {
                val commentDate = comment.commentCreatedAt.date.toFormattedDate()
                if (commentDate != currentDate) {
                    uiModels.add(CommentUiModel.createDateSeparator(commentDate))
                    currentDate = commentDate
                }
                uiModels.add(comment.toUiModel())
            }

            return uiModels
        }
    }
}
