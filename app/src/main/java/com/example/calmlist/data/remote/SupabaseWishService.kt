package com.example.calmlist.data.remote

import android.util.Log
import com.example.calmlist.domain.supabase.SupabaseClient
import com.example.calmlist.model.Wish
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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
            .insert(
                buildJsonObject {
                    put("id", wish.id)
                    put("title", wish.title)
                    put("note", wish.note)
                    put("image_path", wish.imagePath)
                    put("audio_path", wish.audioPath)
                    put("timestamp", wish.timestamp)
                    put("user_id", wish.userId)
                }
            )
    }
    suspend fun updateWish(wish: Wish) {
        Log.d("Update Wish", "updateWish:${wish} ")
        SupabaseClient.client
            .from("wishes")
            .update(
                buildJsonObject {
                    put("title", wish.title)
                    put("note", wish.note)
                    put("image_path", wish.imagePath)
                    put("audio_path", wish.audioPath)
                    put("timestamp", wish.timestamp)
                }
            ) {
                filter {
                    eq("id", wish.id)
                }
            }
    }

    suspend fun deleteWish(wishId: String) {
        Log.d("Update Wish", "updateWish:${wishId} ")
        SupabaseClient.client
            .from("wishes")
            .delete {
                filter {
                    eq("id", wishId)
                }
            }
    }

}