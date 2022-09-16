package com.example.a3dsecurepaymentapp.presentation.secure_payment

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.a3dsecurepaymentapp.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Secure3DPaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _secure3DUrl = mutableStateOf(Secure3DPaymentState())
    val secure3DUrl: State<Secure3DPaymentState> = _secure3DUrl

    init {
        savedStateHandle.get<String>(Constants.PARAM_PAYMENT_URL)?.let { url ->
            _secure3DUrl.value = Secure3DPaymentState(url = url)
        }
    }
}