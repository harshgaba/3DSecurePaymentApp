package com.example.a3dsecurepaymentapp.presentation.card_details

import com.example.a3dsecurepaymentapp.R
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.CardScheme
import com.example.a3dsecurepaymentapp.presentation.card_details.InputValidator.getCVVErrorResIdOrNull
import com.example.a3dsecurepaymentapp.presentation.card_details.InputValidator.getCardNumberErrorResIdOrNull
import org.junit.Test
import com.google.common.truth.Truth.assertThat


internal class InputValidatorTest {

    @Test
    fun `check if card scheme is Amex and CVV is short, returns error id`() {
        val result = getCVVErrorResIdOrNull("122", CardScheme.AMEX)
        assertThat(result).isEqualTo(R.string.cvv_too_short)
    }

    @Test
    fun `check if card scheme is Amex and CVV is valid, returns null`() {
        val result = getCVVErrorResIdOrNull("1223", CardScheme.AMEX)
        assertThat(result).isNull()
    }

    @Test
    fun `check if card scheme is Other type and CVV is short, returns error id`() {
        val result = getCVVErrorResIdOrNull("12", CardScheme.VISA)
        assertThat(result).isEqualTo(R.string.cvv_too_short)
    }

    @Test
    fun `check if card scheme is Other type and CVV is valid, returns null`() {
        val result = getCVVErrorResIdOrNull("122", CardScheme.VISA)
        assertThat(result).isNull()
    }

    @Test
    fun `check if card scheme is Amex and card number is short, returns error id`() {
        val result = getCardNumberErrorResIdOrNull("12289878", CardScheme.AMEX)
        assertThat(result).isEqualTo(R.string.card_number_too_short)
    }
    @Test
    fun `check if card scheme is Dinner and card number is short, returns error id`() {
        val result = getCardNumberErrorResIdOrNull("12289878", CardScheme.DINERS_CLUB)
        assertThat(result).isEqualTo(R.string.card_number_too_short)
    }
    @Test
    fun `check if card scheme is Other type and card number is short, returns error id`() {
        val result = getCardNumberErrorResIdOrNull("12289878", CardScheme.VISA)
        assertThat(result).isEqualTo(R.string.card_number_too_short)
    }

    @Test
    fun `check if card scheme is Amex and card number is valid, returns null`() {
        val result = getCardNumberErrorResIdOrNull("345678901234564", CardScheme.AMEX)
        assertThat(result).isNull()
    }

}