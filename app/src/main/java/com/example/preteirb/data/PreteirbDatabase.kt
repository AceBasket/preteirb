package com.example.preteirb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemDao
import com.example.preteirb.data.usage.Usage
import com.example.preteirb.data.usage.UsageDao
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UserDao

@Database(
    entities = [User::class, Item::class, Usage::class],
    version = 1,
    exportSchema = false
)
abstract class PreteirbDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao
    abstract fun usageDao(): UsageDao
    
    companion object {
        @Volatile
        private var INSTANCE: PreteirbDatabase? = null
        
        fun getDatabase(context: Context): PreteirbDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PreteirbDatabase::class.java,
                    "preteirb_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}