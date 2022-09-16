package com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth
import org.junit.Test


internal class FormatAmexCardTest {
    private lateinit var formatAmexCard: FormatAmexCard

    @Test
    fun `get valid formatted Amex credit card number`() {
        formatAmexCard = FormatAmexCard()
        val result = formatAmexCard(AnnotatedString("345678901234564"))
        Truth.assertThat(result.text.toString()).isEqualTo("3456-789012-34564")
    }
}