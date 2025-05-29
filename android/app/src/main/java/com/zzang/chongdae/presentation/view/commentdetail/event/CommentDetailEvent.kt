package com.zzang.chongdae.presentation.view.commentdetail.event

sealed class CommentDetailEvent {
    data object ShowUpdateStatusDialog : CommentDetailEvent()

    data object ExitOffering : CommentDetailEvent()

    data object BackPressed : CommentDetailEvent()

    data class ShowError(val message: String) : CommentDetailEvent()

    data class ShowReport(val reportUrlId: Int) : CommentDetailEvent()

    data object ShowAlert : CommentDetailEvent()

    data object AlertCancelled : CommentDetailEvent()

    data class LogAnalytics(val eventId: String) : CommentDetailEvent()

    data object OpenDrawer : CommentDetailEvent()
}
