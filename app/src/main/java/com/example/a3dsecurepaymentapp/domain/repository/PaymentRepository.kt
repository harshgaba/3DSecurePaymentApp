package com.example.a3dsecurepaymentapp.domain.repository

import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.data.remote.dto.PaymentDto

interface PaymentRepository {

    suspend fun initiatePayment(cardDetails: CardDetails): PaymentDto

}