package com.example.a3dsecurepaymentapp.presentation.card_details


sealed class UserInputEvent {
    class Cvv(val input: String) : UserInputEvent()
    class CreditCard(val input: String) : UserInputEvent()
}
