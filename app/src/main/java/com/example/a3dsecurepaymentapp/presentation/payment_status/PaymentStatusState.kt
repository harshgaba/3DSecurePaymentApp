package com.example.a3dsecurepaymentapp.presentation.payment_status

import com.example.a3dsecurepaymentapp.R

data class PaymentStatusState(
    val status: Boolean = false,
    var message: Int = if (status) R.string.payment_successful else R.string.payment_failed,
    var statusIcon: Int = if (status) R.drawable.ic_payment_success else R.drawable.ic_payment_fail
)
