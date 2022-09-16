package com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth
import org.junit.Test


internal class FormatDinnersClubCardTest{
    private lateinit var formatDinnersClubCard: FormatDinnersClubCard

    @Test
    fun `get valid formatted DinnersClubCard credit card number`() {
        formatDinnersClubCard = FormatDinnersClubCard()
        val result = formatDinnersClubCard(AnnotatedString("34567890123456"))
        Truth.assertThat(result.text.toString()).isEqualTo("3456-789012-3456")
    }
}