package com.example.a3dsecurepaymentapp.data.repository

import com.example.a3dsecurepaymentapp.data.remote.PaymentApi
import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.data.remote.dto.PaymentDto
import com.example.a3dsecurepaymentapp.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun initiatePayment(cardDetails: CardDetails): PaymentDto {
        return api.initiatePayment(cardDetails)
    }
}