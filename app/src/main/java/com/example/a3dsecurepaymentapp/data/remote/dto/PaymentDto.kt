package com.example.a3dsecurepaymentapp.data.remote.dto

import com.example.a3dsecurepaymentapp.domain.model.Payment
import com.google.gson.annotations.SerializedName

data class PaymentDto(
    @SerializedName("url")
    val url: String? = null
)

fun PaymentDto.toPayment(): Payment {
    return Payment(url.orEmpty())
}
