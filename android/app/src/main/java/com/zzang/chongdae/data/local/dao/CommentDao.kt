package com.zzang.chongdae.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zzang.chongdae.data.local.model.CommentEntity

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(commentItemEntities: List<CommentEntity>)
    
    @Query("SELECT * FROM comments WHERE offeringId = :offeringId")
    suspend fun getCommentsByOfferingId(offeringId: Long): List<CommentEntity>
    
    @Query("SELECT commentId FROM comments WHERE offeringId = :offeringId ORDER BY commentId DESC LIMIT 1")
    suspend fun getLastCommentIdByOfferingId(offeringId: Long): Long?
}
