package com.example.ema.api.Repository

import android.content.Context
import com.example.ema.api.ContactService
import com.example.ema.api.RetrofitClient
import com.example.ema.data.cookies.CookieDatabase
import com.example.ema.model.ContactDto
import com.example.ema.model.ResponseDto
import retrofit2.awaitResponse

class ContactRepository(context: Context) {

    private val contactApi: ContactService
    private val db: CookieDatabase

    init {
        contactApi = RetrofitClient.retrofit.create(ContactService::class.java)
        db = CookieDatabase(context)
    }

    suspend fun getContactList(): ResponseDto<List<ContactDto>>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val call = contactApi.getUserContacts(token).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addContact(newContact: ContactDto): ResponseDto<ContactDto>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val call = contactApi.addNewContact(token, newContact).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }
}