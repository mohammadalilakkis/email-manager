package com.example.ema.api.Repository

import android.content.Context
import com.example.ema.api.MailService
import com.example.ema.api.RetrofitClient
import com.example.ema.data.cookies.CookieDatabase
import com.example.ema.model.CategoryDto
import com.example.ema.model.MailDto
import com.example.ema.model.MailRecordDto
import com.example.ema.model.ResponseDto
import retrofit2.awaitResponse

class MailRepository(context: Context) {

    private val mailApi: MailService
    private val db: CookieDatabase

    init {
        mailApi = RetrofitClient.retrofit.create(MailService::class.java)
        db = CookieDatabase(context)
    }

    suspend fun sendMail(mail: MailDto): ResponseDto<MailRecordDto>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val call = mailApi.sendEmail(token, mail).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getOutbox(category: String? = null): ResponseDto<List<MailRecordDto>>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val categoryParameter = if (category.isNullOrBlank()) null else category
            val call = mailApi.getOutboxMails(token, categoryParameter).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCategories(): ResponseDto<List<CategoryDto>>? {
        return try {
            val call = mailApi.getMailCategories().awaitResponse()
            if (call.isSuccessful)
                call.body()
            else
                null
        } catch (e: Exception) {
            throw e
        }
    }
}