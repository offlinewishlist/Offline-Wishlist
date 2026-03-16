package com.example.calmlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wishes")
data class WishEntity(

    @PrimaryKey
    val id: String,

    val title: String?,
    val note: String?,
    val imagePath: String?,
    val audioPath: String?,
    val timestamp: Long,
    val userId: String
)