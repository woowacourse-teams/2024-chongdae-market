package com.zzang.chongdae.common.firebase.fcm

enum class NotificationImportance {
    DEFAULT,
    HIGH,
    ;

    companion object {
        fun of(importance: Int): NotificationImportance {
            return when (importance) {
                4 -> DEFAULT
                5 -> HIGH
                else -> error("Notification Importance 에러")
            }
        }
    }
}
