package com.example.ema.model

data class MailDto(
    val id: Long = 0,
    val title: String,
    val body: String,
    val campaigns: List<CampaignDto>,
    val category: CategoryDto? = null
)
