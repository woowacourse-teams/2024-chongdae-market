package com.zzang.chongdae.domain.model

import androidx.annotation.DrawableRes
import com.zzang.chongdae.R

enum class OfferingStatus(
    val statusText: String,
    val buttonText: String,
    @DrawableRes val imageRes: Int,
) {
    Recruiting("인원확정", "일정수정", R.drawable.ic_comment_detail_recruiting),
    Scheduling("일정확정", "일정수정", R.drawable.ic_comment_detail_scheduling),
    Purchasing("구매확정", "일정수정", R.drawable.ic_comment_detail_purchasing),
    Dealing("거래확정", "신고하기", R.drawable.ic_comment_detail_dealing),
    Completed("거래완료", "신고하기", R.drawable.ic_comment_detail_completed),
    ;

    companion object {
        fun nextStatus(currentStatus: OfferingStatus): OfferingStatus {
            return entries[(currentStatus.ordinal + 1) % entries.size]
        }
    }
}
