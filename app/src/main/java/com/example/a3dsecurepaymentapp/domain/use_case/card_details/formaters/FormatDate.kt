package com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import javax.inject.Inject

class FormatDate @Inject constructor() {
    operator fun invoke(text: AnnotatedString): TransformedText {

        val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 && i != 6) out += "/"
        }


        val dateOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 5) return offset+1
                return 7
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 6) return offset-1
                return 5
            }
        }

        return TransformedText(AnnotatedString(out), dateOffsetTranslator)
    }
}