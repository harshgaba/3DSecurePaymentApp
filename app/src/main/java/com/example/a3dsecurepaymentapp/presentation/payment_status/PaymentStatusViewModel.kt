package com.example.a3dsecurepaymentapp.presentation.payment_status

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.a3dsecurepaymentapp.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentStatusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _status = mutableStateOf(PaymentStatusState())
    val status: State<PaymentStatusState> = _status

    init {
        savedStateHandle.get<Boolean>(Constants.PARAM_PAYMENT_STATUS)?.let { status ->
            _status.value = PaymentStatusState(status = status)
        }
    }
}