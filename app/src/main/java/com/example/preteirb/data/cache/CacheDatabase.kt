package com.example.preteirb.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.preteirb.data.cache.items_owned.ItemOwned
import com.example.preteirb.data.cache.items_owned.ItemOwnedDao

private const val DATABASE_NAME = "cache_database"

@Database(
    entities = [ItemOwned::class],
    version = 1,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun itemOwnedDao(): ItemOwnedDao

    companion object {
        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getDatabase(context: Context): CacheDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    CacheDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}