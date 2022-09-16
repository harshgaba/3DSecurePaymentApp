package com.example.a3dsecurepaymentapp.presentation.card_details

import com.example.a3dsecurepaymentapp.R
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.CardScheme
import java.util.*


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
//            !isValidCardNumber(number) -> R.string.invalid_card_number
            else -> null
        }
    }

    /**
     * Identifies if the entered [number] is valid credit card number by using
     * the Luhn's Algorithm
     */
    fun isValidCardNumber(number: String) = run {
        var checksum: Int = 0
        for (i in number.length - 1 downTo 0 step 2) {
            checksum += number[i] - '0'
        }
        for (i in number.length - 2 downTo 0 step 2) {
            val n: Int = (number[i] - '0') * 2
            checksum += if (n > 9) n - 9 else n
        }
        checksum % 10 == 0
    }

    /**
     * returns the error string res id in case of
     * [date] does not match the applied rule.
     */
    fun getExpiryDateErrorResIdOrNull(date: String): Int? {
        return when {
            date.length < 6 -> R.string.invalid_expiry_date
            date.substring(2..5).toInt() < Calendar.getInstance()
                .get(Calendar.YEAR) -> R.string.invalid_expiry_date
            date.substring(0..1).toInt() > 12 -> R.string.invalid_expiry_date
            date.substring(0..1).toInt() < Calendar.getInstance()
                .get(Calendar.MONTH) && date.substring(2..5).toInt() == Calendar.getInstance()
                .get(Calendar.YEAR) -> R.string.invalid_expiry_date
            else -> null
        }

    }
}
