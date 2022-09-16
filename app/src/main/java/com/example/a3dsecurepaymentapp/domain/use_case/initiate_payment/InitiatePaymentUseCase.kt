package com.example.a3dsecurepaymentapp.domain.use_case.initiate_payment

import com.example.a3dsecurepaymentapp.common.Resource
import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.data.remote.dto.toPayment
import com.example.a3dsecurepaymentapp.domain.model.Payment
import com.example.a3dsecurepaymentapp.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class InitiatePaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    operator fun invoke(cardDetails: CardDetails): Flow<Resource<Payment>> =

        channelFlow {
            try {
                send(Resource.Loading<Payment>())
                val payment = repository.initiatePayment(cardDetails).toPayment()
                send(Resource.Success<Payment>(payment))
            } catch (e: Throwable) {
                send(Resource.Error<Payment>(e.localizedMessage ?: "Sorry, Something went wrong!"))
            }
        }
}