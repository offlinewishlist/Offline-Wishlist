package com.example.calmlist.model



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wish(
    val id: String,
    val title: String?,
    val note: String?,
    @SerialName("image_path")
    val imagePath: String?,
    @SerialName("audio_path")
    val audioPath: String?,
    val timestamp: Long,
    @SerialName("user_id")
    val userId: String
)