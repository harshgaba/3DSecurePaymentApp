package com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import javax.inject.Inject

class FormatAmexCard @Inject constructor(){
    operator fun invoke(text: AnnotatedString): TransformedText {

        // Making XXXX-XXXXXX-XXXXX card number
        val trimmed = if (text.text.length >= 15) text.text.substring(0..14) else text.text
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i ==3 || i == 9 && i != 14) out += "-"
        }

        /**
         * The offset translator should ignore the hyphen characters, so conversion from
         *  original offset to transformed text works like
         *  - The 4th char of the original text is 5th char in the transformed text. (i.e original[4th] == transformed[5th]])
         *  - The 11th char of the original text is 13th char in the transformed text. (i.e original[11th] == transformed[13th])
         *  Similarly, the reverse conversion works like
         *  - The 5th char of the transformed text is 4th char in the original text. (i.e  transformed[5th] == original[4th] )
         *  - The 13th char of the transformed text is 11th char in the original text. (i.e transformed[13th] == original[11th])
         */
        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 9) return offset + 1
                if(offset <= 15) return offset + 2
                return 17
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 11) return offset - 1
                if(offset <= 17) return offset - 2
                return 15
            }
        }
        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
    }
}