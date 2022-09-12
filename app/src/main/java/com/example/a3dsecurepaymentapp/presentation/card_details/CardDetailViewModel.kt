package com.example.a3dsecurepaymentapp.presentation.card_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3dsecurepaymentapp.common.Resource
import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.domain.use_case.initiate_payment.InitiatePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val initiatePayment: InitiatePaymentUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CardDetailState())
    val state: State<CardDetailState> = _state


    private fun makePayment(cardDetails: CardDetails) {
        initiatePayment(cardDetails = cardDetails).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CardDetailState(payment = result.data)
                }
                is Resource.Error -> {
                    _state.value = CardDetailState(
                        error = result.message ?: "Sorry, Something went wrong!"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CardDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}