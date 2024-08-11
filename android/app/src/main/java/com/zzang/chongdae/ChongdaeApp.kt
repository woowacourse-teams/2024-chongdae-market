package com.zzang.chongdae

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.zzang.chongdae.data.local.database.AppDatabase
import com.zzang.chongdae.data.local.source.CommentLocalDataSourceImpl
import com.zzang.chongdae.data.local.source.OfferingLocalDataSourceImpl
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.AuthRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.source.CommentRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.AuthRepositoryImpl
import com.zzang.chongdae.data.repository.CommentDetailRepositoryImpl
import com.zzang.chongdae.data.repository.CommentRoomsRepositoryImpl
import com.zzang.chongdae.data.repository.OfferingDetailRepositoryImpl
import com.zzang.chongdae.data.repository.OfferingRepositoryImpl
import com.zzang.chongdae.domain.model.HttpStatusCode
import com.zzang.chongdae.domain.repository.AuthRepository
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.domain.repository.OfferingRepository

class ChongdaeApp : Application() {
    private val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(this) }
    private val networkManager: NetworkManager by lazy { NetworkManager }

    private val offeringDao by lazy { appDatabase.offeringDao() }
    private val commentDao by lazy { appDatabase.commentDao() }

    val offeringRepository: OfferingRepository by lazy {
        OfferingRepositoryImpl(
            offeringLocalDataSource = OfferingLocalDataSourceImpl(offeringDao),
            offeringRemoteDataSource = OfferingRemoteDataSourceImpl(networkManager.offeringService()),
        )
    }

    val commentDetailRepository: CommentDetailRepository by lazy {
        CommentDetailRepositoryImpl(
            offeringLocalDataSource = OfferingLocalDataSourceImpl(offeringDao),
            commentLocalDataSource = CommentLocalDataSourceImpl(commentDao),
            commentRemoteDataSource =
                CommentRemoteDataSourceImpl(
                    networkManager.offeringService(),
                    networkManager.commentService(),
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

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        FirebaseApp.initializeApp(this)
    }

    companion object {
        val Context.dataStore by preferencesDataStore(name = "member_preferences")
    }
}
