package com.example.a3dsecurepaymentapp.presentation.card_details

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.SavedStateHandle
import com.example.a3dsecurepaymentapp.R
import com.example.a3dsecurepaymentapp.data.repository.FakePaymentRepository
import com.example.a3dsecurepaymentapp.data.repository.FakeRepoStatus
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatAmexCard
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatDinnersClubCard
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatOtherCards
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.IdentifyCardScheme
import com.example.a3dsecurepaymentapp.domain.use_case.initiate_payment.InitiatePaymentUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


internal class CardDetailViewModelTest {
    private lateinit var initiatePaymentUseCase: InitiatePaymentUseCase
    private lateinit var fakePaymentRepository: FakePaymentRepository
    private lateinit var cardDetailViewModel: CardDetailViewModel

    @Before
    fun setUp() {
        fakePaymentRepository = FakePaymentRepository(FakeRepoStatus.THROW_ERROR)
        initiatePaymentUseCase = InitiatePaymentUseCase(fakePaymentRepository)
        cardDetailViewModel = CardDetailViewModel(
            initiatePayment = initiatePaymentUseCase,
            formatAmexCard = FormatAmexCard(),
            formatDinnersClubCard = FormatDinnersClubCard(),
            formatOtherCards = FormatOtherCards(),
            identifyCardScheme = IdentifyCardScheme(),
            handle = SavedStateHandle()
        )
    }

    @Test
    fun `get input fields validation error message, returns null`() {
        val inputErrors = cardDetailViewModel.getInputErrorsOrNull("234", "4242424242424242")
        assertThat(inputErrors).isNull()
    }

    @Test
    fun `get input fields validation error message, returns message ids`() {
        val inputErrors = cardDetailViewModel.getInputErrorsOrNull("", "")
        assertThat(inputErrors).isNotNull()
        assertThat(inputErrors?.cvvErrorId).isEqualTo(R.string.cvv_too_short)
        assertThat(inputErrors?.cardErrorId).isEqualTo(R.string.card_number_too_short)
    }

    @Test
    fun `get transformed credit card number`() {
        val result =
            cardDetailViewModel.getTransformedCardNumber(AnnotatedString("4242424242424242"))
        assertThat(result.text.toString()).isEqualTo("4242-4242-4242-4242")
    }
}