package com.zzang.chongdae.common.firebase.fcm

import android.app.NotificationManager

sealed interface NotificationImportance {
    val importance: Int
    val channelId: String
    val channelName: String

    data object Default : NotificationImportance {
        override val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
        override val channelId: String = "channel_importance_default"
        override val channelName: String = "Default Channel"
    }

    data object High : NotificationImportance {
        override val importance: Int = NotificationManager.IMPORTANCE_HIGH
        override val channelId: String = "channel_importance_high"
        override val channelName: String = "High Channel"
    }
}
