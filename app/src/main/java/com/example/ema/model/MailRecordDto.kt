package com.example.ema.model

import java.sql.Timestamp

data class MailRecordDto(
    val id: Long,
    val sentDate: Timestamp,
    val mail: MailDto,
    val client: ClientDto,
    val campaigns: List<CampaignDto>,
    val category: CategoryDto
)
