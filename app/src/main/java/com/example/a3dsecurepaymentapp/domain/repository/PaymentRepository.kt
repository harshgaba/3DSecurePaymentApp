package com.example.a3dsecurepaymentapp.domain.repository

import com.example.a3dsecurepaymentapp.data.remote.dto.CardDetailsRequestDto
import com.example.a3dsecurepaymentapp.data.remote.dto.PaymentDto

interface PaymentRepository {

    suspend fun initiatePayment(cardDetailsRequestDto: CardDetailsRequestDto): PaymentDto

}