package com.example.ema.api

import com.example.ema.model.ClientDto
import com.example.ema.model.ResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("account/signup")
    fun signupUser(@Body userData: ClientDto): Call<ResponseDto<ClientDto>>

    @POST("account/login")
    fun loginUser(@Body userData: ClientDto): Call<ResponseDto<ClientDto>>

}