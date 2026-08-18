package com.example.ema.api

import com.example.ema.model.ContactDto
import com.example.ema.model.ResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ContactService {

    @GET("contact/")
    fun getUserContacts(@Header("Authorization") token: String): Call<ResponseDto<List<ContactDto>>>

    @POST("contact/new")
    fun addNewContact(
        @Header("Authorization") token: String,
        @Body contactData: ContactDto
    ): Call<ResponseDto<ContactDto>>

}