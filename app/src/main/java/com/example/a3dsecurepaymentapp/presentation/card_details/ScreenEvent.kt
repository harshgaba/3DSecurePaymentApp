package com.example.a3dsecurepaymentapp.presentation.card_details

import androidx.compose.ui.focus.FocusDirection

sealed class ScreenEvent {
    class UpdateKeyboard(val show: Boolean) : ScreenEvent()
    class RequestFocus(val textFieldKey: FocusedTextFieldKey) : ScreenEvent()
    object ClearFocus : ScreenEvent()
    class MoveFocus(val direction: FocusDirection = FocusDirection.Down) : ScreenEvent()
}
