package com.zzang.chongdae.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zzang.chongdae.data.local.dao.CommentDao
import com.zzang.chongdae.data.local.dao.OfferingDao
import com.zzang.chongdae.data.local.model.CommentEntity
import com.zzang.chongdae.data.local.model.OfferingEntity

@Database(
    entities = [
        OfferingEntity::class,
        CommentEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun offeringDao(): OfferingDao
    
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chongdae_database",
                ).addCallback(
                    object : Callback() {},
                ).build().also { instance = it }
            }
        }
    }
}
