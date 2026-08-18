package com.example.ema.api

import com.example.ema.model.CampaignDto
import com.example.ema.model.ResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface CampaignService {

    @GET("campaign/")
    fun getClientCampaigns(
        @Header("Authorization") token: String
    ): Call<ResponseDto<List<CampaignDto>>>

    @POST("campaign/create")
    fun createCampaign(
        @Header("Authorization") token: String,
        @Body campaignData: CampaignDto
    ): Call<ResponseDto<CampaignDto>>

    @PUT("campaign/add")
    fun addContactToCampaign(
        @Header("Authorization") token: String,
        @Body campaignData: CampaignDto
    ): Call<ResponseDto<CampaignDto>>

}