package com.zzang.chongdae.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = OfferingEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("offeringId"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val commentId: Long = 0,
    val offeringId: Long,
    val content: String,
    val isMine: Boolean,
    val isProposer: Boolean,
    val nickname: String,
    val commentCreatedAtDate: String,
    val commentCreatedAtTime: String,
)
