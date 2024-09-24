package com.zzang.chongdae.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zzang.chongdae.local.model.OfferingEntity

@Dao
interface OfferingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffering(offering: OfferingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(offerings: List<OfferingEntity>)

    @Query("DELETE FROM offerings WHERE id = :id")
    suspend fun deleteOfferingById(id: Long)

    @Query("SELECT * FROM offerings")
    suspend fun getAllOfferings(): List<OfferingEntity>
}
