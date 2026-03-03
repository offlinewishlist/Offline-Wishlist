package com.example.calmlist.data.local.Database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calmlist.data.local.Dao.WishDao
import com.example.calmlist.data.local.entity.WishEntity


@Database(
    entities = [WishEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WishDatabase : RoomDatabase() {

    abstract fun wishDao(): WishDao

    companion object {
        @Volatile
        private var INSTANCE: WishDatabase? = null

        fun getDatabase(context: Context): WishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WishDatabase::class.java,
                    "wishlist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}