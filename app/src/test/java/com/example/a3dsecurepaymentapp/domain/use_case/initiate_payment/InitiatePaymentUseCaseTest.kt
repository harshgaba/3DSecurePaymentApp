package com.example.a3dsecurepaymentapp.domain.use_case.initiate_payment

import com.google.common.truth.Truth.assertThat

import com.example.a3dsecurepaymentapp.common.Constants
import com.example.a3dsecurepaymentapp.data.repository.FakePaymentRepository
import com.example.a3dsecurepaymentapp.data.repository.FakeRepoStatus
import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.domain.model.Payment
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


internal class InitiatePaymentUseCaseTest {

    private lateinit var cardDetails: CardDetails

    @Before
    fun setUp() {
        cardDetails = CardDetails(
            expiryMonth = "06",
            expiryYear = "2032",
            cvv = "123",
            number = "424242424242"
        )
    }

    @Test
    fun `initiate payment, returns error`() = runBlocking {
        val initiatePayment =
            InitiatePaymentUseCase(FakePaymentRepository(FakeRepoStatus.THROW_ERROR))
        val value =
            initiatePayment(cardDetails).filter { !it.message.isNullOrBlank() }.first()
        assertThat(value.message).isEqualTo("Something went wrong!")
    }

    @Test
    fun `initiate payment, returns secure3d url`() = runBlocking {
        val initiatePayment =
            InitiatePaymentUseCase(FakePaymentRepository(FakeRepoStatus.SHOW_DATA))
        val value = initiatePayment(cardDetails).filter { it.data != null }.first()
        assertThat(value.data).isEqualTo(Payment(Constants.SUCCESS_URL))
    }


}