package com.example.a3dsecurepaymentapp.presentation.card_details

import com.example.a3dsecurepaymentapp.R
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.CardScheme
import com.example.a3dsecurepaymentapp.presentation.card_details.InputValidator.getCVVErrorResIdOrNull
import com.example.a3dsecurepaymentapp.presentation.card_details.InputValidator.getCardNumberErrorResIdOrNull
import com.example.a3dsecurepaymentapp.presentation.card_details.InputValidator.getExpiryDateErrorResIdOrNull
import com.example.a3dsecurepaymentapp.presentation.card_details.InputValidator.isValidCardNumber
import org.junit.Test
import com.google.common.truth.Truth.assertThat


internal classInputValidatorTest {

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
    @Test
    fun `check if card number is valid, returns true`() {
        val result = isValidCardNumber("345678901234564")
        assertThat(result).isEqualTo(true)
    }
    @Test
    fun `check if card number is valid, returns false`() {
        val result = isValidCardNumber("1010101010101010")
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `check when date is valid, returns null`() {
        val result = getExpiryDateErrorResIdOrNull("022033")
        assertThat(result).isNull()
    }

    @Test
    fun `check when date is short, returns error id`() {
        val result = getExpiryDateErrorResIdOrNull("02")
        assertThat(result).isEqualTo(R.string.invalid_expiry_date)
    }

    @Test
    fun `check when date is with invalid month, returns error id`() {
        val result = getExpiryDateErrorResIdOrNull("22")
        assertThat(result).isEqualTo(R.string.invalid_expiry_date)
    }

    @Test
    fun `check when date is with invalid year, returns error id`() {
        val result = getExpiryDateErrorResIdOrNull("1999")
        assertThat(result).isEqualTo(R.string.invalid_expiry_date)
    }

    @Test
    fun `check when date is with valid year but past month, returns error id`() {
        val result = getExpiryDateErrorResIdOrNull("012022")
        assertThat(result).isEqualTo(R.string.invalid_expiry_date)
    }


}