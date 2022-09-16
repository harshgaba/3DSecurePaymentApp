package com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth
import org.junit.Test


internal class FormatOtherCardsTest{
    private lateinit var formatOtherCards: FormatOtherCards

    @Test
    fun `get valid formatted Other credit card number`() {
        formatOtherCards = FormatOtherCards()
        val result = formatOtherCards(AnnotatedString("3456789012345690"))
        Truth.assertThat(result.text.toString()).isEqualTo("3456-7890-1234-5690")
    }
}