package com.zzang.chongdae.common.firebase.fcm

enum class NotificationType {
    COMMENT_DETAIL,
    OFFERING_DETAIL,
    ;

    companion object {
        private const val COMMENT_DETAIL_KEY = "comment_detail"
        private const val OFFERING_DETAIL_KEY = "offering_detail"

        fun of(type: String?): NotificationType {
            return when (type) {
                COMMENT_DETAIL_KEY -> COMMENT_DETAIL
                OFFERING_DETAIL_KEY -> OFFERING_DETAIL
                else -> error("알림 타입이 유효하지 않음")
            }
        }
    }
}
