package com.example.calmlist.data.local.Dao

import androidx.room.*
import com.example.calmlist.data.local.entity.WishEntity

import kotlinx.coroutines.flow.Flow


@Dao
interface WishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wishes: List<WishEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWish(wish: WishEntity)
    @Query("DELETE FROM wishes WHERE id = :wishId")
    suspend fun deleteWishById(wishId: String)

    @Query("SELECT * FROM wishes WHERE userId = :userId ORDER BY timestamp DESC")
    fun getWishesByUser(userId: String): Flow<List<WishEntity>>

    @Query("SELECT * FROM wishes WHERE id = :wishId")
    suspend fun getWishById(wishId: String): WishEntity?

    @Delete
    suspend fun deleteWish(wish: WishEntity)

    @Query("DELETE FROM wishes WHERE userId = :userId")
    suspend fun clearUserWishes(userId: String)

    @Query("DELETE FROM wishes")
    suspend fun deleteAll()
}