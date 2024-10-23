package com.zzang.chongdae.common.firebase.fcm

import android.app.NotificationManager

sealed interface NotificationImportance {
    val importance: Int
    val channelId: String
    val channelName: String

    data object Default : NotificationImportance {
        override val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
        override val channelId: String = CHANNEL_ID_IMPORTANCE_DEFAULT
        override val channelName: String = CHANNEL_NAME_IMPORTANCE_DEFAULT
    }

    data object High : NotificationImportance {
        override val importance: Int = NotificationManager.IMPORTANCE_HIGH
        override val channelId: String = CHANNEL_ID_IMPORTANCE_HIGH
        override val channelName: String = CHANNEL_NAME_IMPORTANCE_HIGH
    }

    companion object {
        private const val CHANNEL_ID_IMPORTANCE_DEFAULT = "channel_importance_default"
        private const val CHANNEL_NAME_IMPORTANCE_DEFAULT = "Default Channel"
        private const val CHANNEL_ID_IMPORTANCE_HIGH = "channel_importance_high"
        private const val CHANNEL_NAME_IMPORTANCE_HIGH = "High Channel"
    }
}
