package com.zzang.chongdae.presentation.view.commentdetail.adapter.comment

sealed class CommentViewType {
    data object MyComment : CommentViewType()

    data object OtherComment : CommentViewType()

    data object DateSeparator : CommentViewType()
}
