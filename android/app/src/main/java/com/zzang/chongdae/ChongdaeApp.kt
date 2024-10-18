package com.zzang.chongdae

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.zzang.chongdae.data.local.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChongdaeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        FirebaseApp.initializeApp(this)
        _chongdaeAppContext = this


        // FCM


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    "MyFirebaseMsgService",
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@addOnCompleteListener
            }

            // FCM 토큰
            val token = task.result
            Log.d("MyFirebaseMsgService", "FCM Token: $token")

            // 서버에 토큰 전송 로직 추가
        }
    }

    companion object {
        val Context.dataStore by preferencesDataStore(name = "member_preferences")

        private lateinit var _chongdaeAppContext: Context
        val chongdaeAppContext get() = _chongdaeAppContext

        private val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(chongdaeAppContext) }
        val offeringDao by lazy { appDatabase.offeringDao() }
    }
}
