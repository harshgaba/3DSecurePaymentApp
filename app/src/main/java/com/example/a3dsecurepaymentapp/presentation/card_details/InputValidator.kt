package com.example.a3dsecurepaymentapp.presentation.card_details

import com.example.a3dsecurepaymentapp.R
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.CardScheme


object InputValidator {

    /**
     * returns the error string res id in case of
     * [cvv] length does not match the applied rule.
     */
    fun getCVVErrorResIdOrNull(cvv: String, cardScheme: CardScheme): Int? {
        return when {
            cardScheme == CardScheme.AMEX && cvv.length < 4 -> R.string.cvv_too_short
            cvv.length < 3 -> R.string.cvv_too_short
            else -> null
        }
    }

    /**
     * returns the error string res id in case of
     * [number] length does not match the applied rule.
     */
    fun getCardNumberErrorResIdOrNull(number: String, cardScheme: CardScheme): Int? {
        return when {
            cardScheme == CardScheme.AMEX && number.length < 15 -> R.string.card_number_too_short
            cardScheme == CardScheme.DINERS_CLUB && number.length < 14 -> R.string.card_number_too_short
            (cardScheme != CardScheme.AMEX && cardScheme != CardScheme.DINERS_CLUB)
                    && number.length < 16 -> R.string.card_number_too_short
            else -> null
        }
    }

}
