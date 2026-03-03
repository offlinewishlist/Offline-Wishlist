package com.example.calmlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishes")
data class WishEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String?,
    val note: String?,
    val imagePath: String?,
    val audioPath: String?,
    val timestamp: Long
)