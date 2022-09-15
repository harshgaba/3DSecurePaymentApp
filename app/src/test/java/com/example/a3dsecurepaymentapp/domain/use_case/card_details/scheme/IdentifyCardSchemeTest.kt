package com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme

import com.google.common.truth.Truth.assertThat
import org.junit.Test


internal class IdentifyCardSchemeTest {
    private lateinit var identifyCardScheme: IdentifyCardScheme

    @Test
    fun `check if card number is Amex credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("345678901234564")
        assertThat(result).isEqualTo(CardScheme.AMEX)
    }

    @Test
    fun `check if card number is Mastercard credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("2223000010479399")
        assertThat(result).isEqualTo(CardScheme.MASTERCARD)
    }

    @Test
    fun `check if card number is Visa credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("4543474002249996")
        assertThat(result).isEqualTo(CardScheme.VISA)
    }

    @Test
    fun `check if card number is DinersClub credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("30123456789019")
        assertThat(result).isEqualTo(CardScheme.DINERS_CLUB)
    }

    @Test
    fun `check if card number is Discover credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("6011111111111117")
        assertThat(result).isEqualTo(CardScheme.DISCOVER)
    }

    @Test
    fun `check if card number is JCB credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("3530111333300000")
        assertThat(result).isEqualTo(CardScheme.JCB)
    }

    @Test
    fun `check if card number is Unknown credit card number`() {
        identifyCardScheme = IdentifyCardScheme()
        val result = identifyCardScheme("019109991019019")
        assertThat(result).isEqualTo(CardScheme.UNKNOWN)
    }
}