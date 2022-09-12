package com.example.a3dsecurepaymentapp.data.remote.dto

import com.example.a3dsecurepaymentapp.common.Constants.FAILURE_URL
import com.example.a3dsecurepaymentapp.common.Constants.SUCCESS_URL
import com.google.gson.annotations.SerializedName

data class CardDetailsRequestDto(
    @SerializedName("expiry_month")
    val expiryMonth: String,
    @SerializedName("expiry_year")
    val expiryYear: String,
    @SerializedName("cvv")
    val cvv: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("success_url")
    val successUrl: String = SUCCESS_URL,
    @SerializedName("failure_url")
    val failureUrl: String = FAILURE_URL
)
