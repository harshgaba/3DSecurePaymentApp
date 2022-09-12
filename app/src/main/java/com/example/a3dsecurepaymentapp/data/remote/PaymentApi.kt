package com.example.a3dsecurepaymentapp.data.remote

import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.data.remote.dto.PaymentDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {

    @POST("/pay")
    suspend fun initiatePayment(@Body cardDetails: CardDetails): PaymentDto
}