package com.example.a3dsecurepaymentapp.presentation.secure_payment

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a3dsecurepaymentapp.common.Constants
import com.example.a3dsecurepaymentapp.presentation.Screen

/**
 * This Screen contains WebView, which loads the 3DSecure payment url.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Secure3DPaymentScreen(
    navController: NavController,
    viewModel: Secure3DPaymentViewModel = hiltViewModel()
) {

    val secure3DUrl = viewModel.secure3DUrl.value

    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true

            webViewClient = object : WebViewClient() {

                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.contains(Constants.SUCCESS_URL)) {
                        navController.navigate(Screen.PaymentStatusScreen.route + "?payment_status=${true}") {
                            popUpTo(Screen.Secure3DPaymentScreen.route) {
                                inclusive = true
                            }
                        }
                        return true
                    } else if (url.contains(Constants.FAILURE_URL)) {
                        navController.navigate(Screen.PaymentStatusScreen.route + "?payment_status=${false}") {
                            popUpTo(Screen.Secure3DPaymentScreen.route) {
                                inclusive = true
                            }
                        }
                        return true
                    }
                    return false
                }
            }

            secure3DUrl.url?.let { it1 -> loadUrl(it1) }
        }
    }, update = {
        secure3DUrl.url?.let { it1 -> it.loadUrl(it1) }
    })

    BackHandler(enabled = true) {
        navController.navigate(Screen.PaymentStatusScreen.route + "?payment_status=${false}") {
            popUpTo(Screen.Secure3DPaymentScreen.route) {
                inclusive = true
            }
        }
    }

}