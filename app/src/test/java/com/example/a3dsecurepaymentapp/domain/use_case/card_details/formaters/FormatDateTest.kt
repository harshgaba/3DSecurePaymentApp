package com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth
import org.junit.Test


internal class FormatDateTest{
    private lateinit var formatDate: FormatDate

    @Test
    fun `get valid formatted DinnersClubCard credit card number`() {
        formatDate = FormatDate()
        val result = formatDate(AnnotatedString("022030"))
        Truth.assertThat(result.text.toString()).isEqualTo("02/2030")
    }
}