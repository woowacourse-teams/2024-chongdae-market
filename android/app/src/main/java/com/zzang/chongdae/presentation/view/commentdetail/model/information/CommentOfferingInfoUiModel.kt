package com.zzang.chongdae.presentation.view.commentdetail.model.information

import com.zzang.chongdae.domain.model.comment.CommentOfferingInfo

class CommentOfferingInfoUiModel(
    val status: String,
    val imageUrl: String,
    val buttonText: String,
    val message: String,
    val title: String,
    val isProposer: Boolean,
) {
    companion object {
        fun CommentOfferingInfo.toUiModel(): CommentOfferingInfoUiModel {
            return CommentOfferingInfoUiModel(
                status = status,
                imageUrl = imageUrl,
                buttonText = buttonText,
                message = message,
                title = title,
                isProposer = isProposer,
            )
        }
    }
}
