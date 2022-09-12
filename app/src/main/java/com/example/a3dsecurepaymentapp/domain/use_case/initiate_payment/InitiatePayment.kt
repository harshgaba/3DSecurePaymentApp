package com.example.a3dsecurepaymentapp.domain.use_case.initiate_payment

import com.example.a3dsecurepaymentapp.common.Resource
import com.example.a3dsecurepaymentapp.data.remote.dto.CardDetailsRequestDto
import com.example.a3dsecurepaymentapp.data.remote.dto.toPayment
import com.example.a3dsecurepaymentapp.domain.model.Payment
import com.example.a3dsecurepaymentapp.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class InitiatePayment @Inject constructor(
    private val repository: PaymentRepository
) {
    operator fun invoke(cardDetailsRequestDto: CardDetailsRequestDto): Flow<Resource<Payment>> =
        flow {
            try {
                emit(Resource.Loading<Payment>())
                val payment = repository.initiatePayment(cardDetailsRequestDto).toPayment()
                emit(Resource.Success<Payment>(payment))
            } catch (e: HttpException) {
                emit(Resource.Error<Payment>(e.localizedMessage ?: "Sorry, Something went wrong!"))
            } catch (e: IOException) {
                emit(Resource.Error<Payment>("Couldn't reach server. Check your internet connection."))
            }
        }
}