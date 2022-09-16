package com.example.a3dsecurepaymentapp.presentation

sealed class Screen(val route: String) {
    object CardDetailScreen: Screen("card_detail_screen")
    object PaymentStatusScreen: Screen("payment_status_screen")
    object Secure3DPaymentScreen: Screen("secure_3d_payment_screen")
}