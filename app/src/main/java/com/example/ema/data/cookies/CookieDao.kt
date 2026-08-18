package com.example.ema.data.cookies

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CookieDao {
    @Query("SELECT * FROM cookies WHERE name = :cookieName")
    fun getCookieByName(cookieName: String): CookieEntity?

    @Query("SELECT * FROM cookies")
    fun getAllCookies(): List<CookieEntity>

    @Insert
    fun insertCookie(cookie: CookieEntity)

    @Delete
    fun deleteCookie(cookie: CookieEntity)

    @Query("DELETE FROM cookies")
    fun deleteAllCookies()

    @Update
    fun updateCookie(vararg cookies: CookieEntity)
}