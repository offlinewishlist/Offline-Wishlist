package com.example.calmlist.data.local.Dao

import androidx.room.*
import com.example.calmlist.data.local.entity.WishEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface WishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWish(wish: WishEntity)

    @Query("SELECT * FROM wishes ORDER BY timestamp DESC")
    fun getAllWishes(): Flow<List<WishEntity>>

    @Query("SELECT * FROM wishes WHERE id = :wishId")
    suspend fun getWishById(wishId: Int): WishEntity?

    @Delete
    suspend fun deleteWish(wish: WishEntity)

    @Query("DELETE FROM wishes")
    suspend fun clearAllWishes()
}