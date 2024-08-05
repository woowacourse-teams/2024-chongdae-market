package com.zzang.chongdae

import android.app.Application
import com.zzang.chongdae.data.local.database.AppDatabase
import com.zzang.chongdae.data.local.source.CommentLocalDataSourceImpl
import com.zzang.chongdae.data.local.source.OfferingLocalDataSourceImpl
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.CommentRemoteDataSourceImpl
import com.zzang.chongdae.data.remote.source.CommentRoomsDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingDetailDataSourceImpl
import com.zzang.chongdae.data.remote.source.OfferingRemoteDataSourceImpl
import com.zzang.chongdae.data.repository.CommentDetailRepositoryImpl
import com.zzang.chongdae.data.repository.CommentRoomsRepositoryImpl
import com.zzang.chongdae.data.repository.OfferingDetailRepositoryImpl
import com.zzang.chongdae.data.repository.OfferingsRepositoryImpl
import com.zzang.chongdae.domain.repository.CommentDetailRepository
import com.zzang.chongdae.domain.repository.CommentRoomsRepository
import com.zzang.chongdae.domain.repository.OfferingDetailRepository
import com.zzang.chongdae.domain.repository.OfferingsRepository

class ChongdaeApp : Application() {
    private val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(this) }
    private val networkManager: NetworkManager by lazy { NetworkManager }

    private val offeringDao by lazy { appDatabase.offeringDao() }
    private val commentDao by lazy { appDatabase.commentDao() }

    val offeringRepository: OfferingsRepository by lazy {
        OfferingsRepositoryImpl(
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

    override fun onCreate() {
        super.onCreate()
    }
}
