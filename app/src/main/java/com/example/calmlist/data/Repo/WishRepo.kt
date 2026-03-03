package com.example.calmlist.data.Repo

import com.example.calmlist.data.local.Dao.WishDao
import com.example.calmlist.data.local.entity.WishEntity
import com.example.calmlist.model.Wish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishRepository(private val dao: WishDao) {

    fun getAllWishes(): Flow<List<Wish>> {
        return dao.getAllWishes().map { list ->
            list.map { entity ->
                Wish(
                    id = entity.id,
                    title = entity.title,
                    note = entity.note,
                    imagePath = entity.imagePath,
                    audioPath = entity.audioPath,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun insertWish(wish: Wish) {
        dao.insertWish(
            WishEntity(
                id = wish.id,
                title = wish.title,
                note = wish.note,
                imagePath = wish.imagePath,
                audioPath = wish.audioPath,
                timestamp = wish.timestamp
            )
        )
    }

    suspend fun deleteWish(wish: Wish) {
        dao.deleteWish(
            WishEntity(
                id = wish.id,
                title = wish.title,
                note = wish.note,
                imagePath = wish.imagePath,
                audioPath = wish.audioPath,
                timestamp = wish.timestamp
            )
        )
    }

    suspend fun clearAll() {
        dao.clearAllWishes()
    }
}