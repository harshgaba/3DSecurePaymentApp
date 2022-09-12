package com.example.a3dsecurepaymentapp.presentation.card_details

import com.example.a3dsecurepaymentapp.domain.model.Payment

data class CardDetailState(
    val isLoading: Boolean = false,
    val payment: Payment? = null,
    val error: String = ""
)
