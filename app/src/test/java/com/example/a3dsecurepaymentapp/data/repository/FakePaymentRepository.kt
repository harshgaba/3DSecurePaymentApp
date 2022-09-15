package com.example.a3dsecurepaymentapp.data.repository

import com.example.a3dsecurepaymentapp.common.Constants
import com.example.a3dsecurepaymentapp.data.remote.dto.PaymentDto
import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.domain.repository.PaymentRepository

class FakePaymentRepository constructor(private val status: FakeRepoStatus) : PaymentRepository {

    override suspend fun initiatePayment(cardDetails: CardDetails): PaymentDto {
        return when (status) {
            FakeRepoStatus.THROW_ERROR -> {
                throw Exception("Something went wrong!")
            }
            FakeRepoStatus.SHOW_DATA -> {
                PaymentDto(Constants.SUCCESS_URL)
            }
        }
    }
}