package com.zzang.chongdae.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offerings")
data class OfferingEntity(
    @PrimaryKey val id: Long,
)
