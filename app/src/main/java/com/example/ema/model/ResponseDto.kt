package com.example.ema.model

data class ResponseDto<T> (
    val message: String,
    val result: T
)
