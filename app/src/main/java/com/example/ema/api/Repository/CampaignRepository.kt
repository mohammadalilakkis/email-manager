package com.example.ema.api.Repository

import android.content.Context
import com.example.ema.api.CampaignService
import com.example.ema.api.RetrofitClient
import com.example.ema.data.cookies.CookieDatabase
import com.example.ema.model.CampaignDto
import com.example.ema.model.ResponseDto
import retrofit2.awaitResponse

class CampaignRepository(context: Context) {

    private val campaignApi: CampaignService
    private val db: CookieDatabase

    init {
        campaignApi = RetrofitClient.retrofit.create(CampaignService::class.java)
        db = CookieDatabase(context)
    }

    suspend fun getClientCampaigns(): ResponseDto<List<CampaignDto>>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val call = campaignApi.getClientCampaigns(token).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun createCampaign(campaignData: CampaignDto): ResponseDto<CampaignDto>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val call = campaignApi.createCampaign(token, campaignData).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addContactToCampaign(campaignData: CampaignDto): ResponseDto<CampaignDto>? {
        return try {
            val token = ("Bearer " + db.cookieDao().getCookieByName("token")?.value)
            val call = campaignApi.addContactToCampaign(token, campaignData).awaitResponse()
            if (call.isSuccessful) {
                call.body()
            } else null
        } catch (e: Exception) {
            throw e
        }
    }
}