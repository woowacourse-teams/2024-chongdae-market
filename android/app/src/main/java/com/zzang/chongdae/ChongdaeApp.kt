package com.zzang.chongdae

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.zzang.chongdae.data.local.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChongdaeApp : Application() {
    // 안드코드 수정
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        FirebaseApp.initializeApp(this)
        _chongdaeAppContext = this
    }

    companion object {
        val Context.dataStore by preferencesDataStore(name = "member_preferences")

        private lateinit var _chongdaeAppContext: Context
        val chongdaeAppContext get() = _chongdaeAppContext

        private val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(chongdaeAppContext) }
        val offeringDao by lazy { appDatabase.offeringDao() }
    }
}
