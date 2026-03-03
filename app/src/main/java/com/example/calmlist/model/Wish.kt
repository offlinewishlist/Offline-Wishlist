package com.example.calmlist.model


data class Wish(
    val id: Int,
    val title: String?,
    val note: String?,
    val imagePath: String?,
    val audioPath: String?,
    val timestamp: Long
)