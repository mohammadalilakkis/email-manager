package com.example.ema.model

data class CampaignDto(
    val id: Long = 0,
    val title: String,
    val description: String,
    val client: ClientDto? = null,
    var contacts: MutableList<ContactDto>
)
