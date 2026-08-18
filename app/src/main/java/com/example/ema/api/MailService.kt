package com.example.ema.api

import com.example.ema.model.CategoryDto
import com.example.ema.model.MailDto
import com.example.ema.model.MailRecordDto
import com.example.ema.model.ResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MailService {

    @POST("mail/send")
    fun sendEmail(
        @Header("Authorization") token: String,
        @Body mailData: MailDto
    ): Call<ResponseDto<MailRecordDto>>

    @GET("mail/outbox")
    fun getOutboxMails(
        @Header("Authorization") token: String,
        @Query("c") c: String? = null
    ): Call<ResponseDto<List<MailRecordDto>>>

    @GET("mail/category")
    fun getMailCategories(): Call<ResponseDto<List<CategoryDto>>>

}