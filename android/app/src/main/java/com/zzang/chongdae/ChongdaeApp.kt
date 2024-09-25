package com.zzang.chongdae

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.zzang.chongdae.auth.repository.AuthRepository
import com.zzang.chongdae.auth.repository.AuthRepositoryImpl
import com.zzang.chongdae.auth.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.data.local.database.AppDatabase
import com.zzang.chongdae.data.local.source.OfferingLocalDataSourceImpl
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.CommentRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.source.ParticipantRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.CommentDetailRepositoryImpl
import com.zzang.chongdae.data.repository.CommentRoomsRepositoryImpl
import com.zzang.chongdae.data.repository.OfferingDetailRepositoryImpl
import com.zzang.chongdae.data.repository.OfferingRepositoryImpl
import com.zzang.chongdae.data.repository.ParticipantRepositoryImpl
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository
import com.zzang.chongdae.domain.repository.ParticipantRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChongdaeApp : Application() {
    private val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(this) }
    private val networkManager: NetworkManager by lazy { NetworkManager }

    private val offeringDao by lazy { appDatabase.offeringDao() }

    val offeringRepository: OfferingRepository by lazy {
        OfferingRepositoryImpl(
//            offeringLocalDataSource = OfferingLocalDataSourceImpl(offeringDao),
            offeringRemoteDataSource = OfferingRemoteDataSourceImpl(networkManager.offeringService()),
        )
    }

    val commentDetailRepository: CommentDetailRepository by lazy {
        CommentDetailRepositoryImpl(
            commentRemoteDataSource =
                CommentRemoteDataSourceImpl(
                    service = networkManager.commentService(),
                ),
        )
    }

    val commentRoomsRepository: CommentRoomsRepository by lazy {
        CommentRoomsRepositoryImpl(
            commentRoomsDataSource = CommentRoomsDataSourceImpl(networkManager.commentService()),
        )
    }

    val offeringDetailRepository: OfferingDetailRepository by lazy {
        OfferingDetailRepositoryImpl(
            offeringDetailDataSource =
                OfferingDetailDataSourceImpl(
                    networkManager.offeringService(),
                    networkManager.participationService(),
                ),
        )
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            authRemoteDataSource =
                AuthRemoteDataSourceImpl(
                    networkManager.authService(),
                ),
        )
    }

    val participantRepository: ParticipantRepository by lazy {
        ParticipantRepositoryImpl(
            participantRemoteDataSource =
                ParticipantRemoteDataSourceImpl(
                    networkManager.participationService(),
                ),
        )
    }

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
    }
}
