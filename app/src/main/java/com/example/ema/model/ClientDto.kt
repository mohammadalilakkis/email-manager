package com.example.ema.model

data class ClientDto (
    val id: Long = 0,
    val username: String,
    val password: String,
    val email: String,
    val name: String
)
