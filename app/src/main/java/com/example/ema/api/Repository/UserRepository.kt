package com.example.ema.api.Repository

import android.content.Context
import com.example.ema.api.RetrofitClient
import com.example.ema.api.UserService
import com.example.ema.data.cookies.CookieDatabase
import com.example.ema.data.cookies.CookieEntity
import com.example.ema.model.ClientDto
import com.example.ema.model.ResponseDto
import retrofit2.awaitResponse
import java.sql.Timestamp

class UserRepository(context: Context) {

    private val userApi: UserService
    private val db: CookieDatabase

    init {

        userApi = RetrofitClient.retrofit.create(UserService::class.java)
        db = CookieDatabase(context)
    }

    suspend fun signupUser(client: ClientDto): ResponseDto<ClientDto>? {
        return try {
            val call = userApi.signupUser(client).awaitResponse()
            if (call.isSuccessful) {
                val token: String = call.headers().get("Set-Cookie") ?: ""
                saveToken(token)
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun loginUser(client: ClientDto): ResponseDto<ClientDto>? {
        return try {
            val call = userApi.loginUser(client).awaitResponse()
            if (call.isSuccessful) {
                val token: String = call.headers().get("Set-Cookie") ?: ""
                saveToken(token)
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    private fun saveToken(token: String) {
        db.cookieDao().insertCookie(
            CookieEntity(
                name = "token",
                value = token.substring(token.indexOf("=") + 1)
                    .split(';')[0],
                createAt = Timestamp(System.currentTimeMillis())
            )
        )
    }
}