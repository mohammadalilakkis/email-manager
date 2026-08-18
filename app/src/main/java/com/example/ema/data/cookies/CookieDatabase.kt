package com.example.ema.data.cookies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(TimestampConverter::class)
@Database(entities = [CookieEntity::class], version = 1, exportSchema = false)
abstract class CookieDatabase : RoomDatabase() {
    abstract fun cookieDao(): CookieDao

    companion object {
        @Volatile private var instance: CookieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            CookieDatabase::class.java, "cookie.db")
            .build()
    }
}