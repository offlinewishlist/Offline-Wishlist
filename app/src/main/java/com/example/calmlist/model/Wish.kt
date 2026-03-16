package com.example.calmlist.model



data class Wish(
    val id: String,
    val title: String?,
    val note: String?,
    val imagePath: String?,
    val audioPath: String?,
    val timestamp: Long,
    val userId: String
)