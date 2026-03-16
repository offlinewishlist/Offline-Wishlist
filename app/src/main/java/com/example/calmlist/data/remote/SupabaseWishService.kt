package com.example.calmlist.data.remote

import com.example.calmlist.domain.supabase.SupabaseClient
import com.example.calmlist.model.Wish
import io.github.jan.supabase.postgrest.from


class SupabaseWishService {

    suspend fun fetchWishes(userId: String): List<Wish> {
        return SupabaseClient.client
            .from("wishes")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList()
    }

    suspend fun uploadWish(wish: Wish) {
        SupabaseClient.client
            .from("wishes")
            .insert(wish)
    }
    suspend fun updateWish(wish: Wish) {
        SupabaseClient.client
            .from("wishes")
            .update(
                mapOf(
                    "title" to wish.title,
                    "note" to wish.note,
                    "image_path" to wish.imagePath,
                    "audio_path" to wish.audioPath,
                    "timestamp" to wish.timestamp
                )
            ) {
                filter {
                    eq("id", wish.id)
                }
            }
    }

    suspend fun deleteWish(wishId: String) {
        SupabaseClient.client
            .from("wishes")
            .delete {
                filter {
                    eq("id", wishId)
                }
            }
    }

}