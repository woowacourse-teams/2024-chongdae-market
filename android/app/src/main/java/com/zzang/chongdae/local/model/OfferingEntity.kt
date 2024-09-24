package com.zzang.chongdae.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offerings")
data class OfferingEntity(
    @PrimaryKey val id: Long,
)
