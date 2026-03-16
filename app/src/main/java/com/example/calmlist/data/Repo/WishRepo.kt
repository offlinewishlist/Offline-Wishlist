package com.example.calmlist.data.Repo

import com.example.calmlist.data.local.Dao.WishDao
import com.example.calmlist.data.local.entity.WishEntity
import com.example.calmlist.data.remote.SupabaseWishService
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.Wish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
class WishRepository(
    private val dao: WishDao,
    private val remote: SupabaseWishService
) {

    fun syncFromCloud(userId: String): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)

        try {
            val cloudData = remote.fetchWishes(userId)

            val entities = cloudData.map {
                WishEntity(
                    id = it.id,
                    title = it.title,
                    note = it.note,
                    imagePath = it.imagePath,
                    audioPath = it.audioPath,
                    timestamp = it.timestamp,
                    userId = it.userId
                )
            }

            dao.insertAll(entities)
            emit(ResultState.Succes(Unit))

        } catch (e: Exception) {
            emit(ResultState.error(e.message ?: "Cloud sync failed"))
        }
    }

    fun getLocalWishes(userId: String): Flow<List<Wish>> {
        return dao.getWishesByUser(userId).map { list ->
            list.map {
                Wish(
                    id = it.id,
                    title = it.title,
                    note = it.note,
                    imagePath = it.imagePath,
                    audioPath = it.audioPath,
                    timestamp = it.timestamp,
                    userId = it.userId
                )
            }
        }
    }

    suspend fun saveWishOffline(wish: Wish) {
        dao.insertWish(
            WishEntity(
                id = wish.id,
                title = wish.title,
                note = wish.note,
                imagePath = wish.imagePath,
                audioPath = wish.audioPath,
                timestamp = wish.timestamp,
                userId = wish.userId
            )
        )
    }

    suspend fun pushWishOnline(wish: Wish) {
        remote.uploadWish(wish)
    }fun editWish(wish: Wish): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)

        try {
            dao.insertWish(
                WishEntity(
                    id = wish.id,
                    title = wish.title,
                    note = wish.note,
                    imagePath = wish.imagePath,
                    audioPath = wish.audioPath,
                    timestamp = wish.timestamp,
                    userId = wish.userId
                )
            )

            remote.updateWish(wish)

            emit(ResultState.Succes(Unit))
        } catch (e: Exception) {
            emit(ResultState.error(e.message ?: "Edit failed"))
        }
    }

    fun deleteWish(wishId: String): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)

        try {
            dao.deleteWishById(wishId)

            remote.deleteWish(wishId)

            emit(ResultState.Succes(Unit))
        } catch (e: Exception) {
            emit(ResultState.error(e.message ?: "Delete failed"))
        }
    }
}