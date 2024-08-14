package com.zzang.chongdae.presentation.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsManager(private val firebaseAnalytics: FirebaseAnalytics) {
    fun logSelectContentEvent(
        id: String,
        name: String,
        contentType: String,
    ) {
        val bundle =
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, id)
                putString(FirebaseAnalytics.Param.ITEM_NAME, name)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
            }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun logScreenView(
        screenName: String,
        screenClass: String,
    ) {
        val bundle =
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
