package com.example.a3dsecurepaymentapp.presentation.payment_status

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

/**
 * This Screen contains status of Payment,
 * which either loads the Success state or Failure state.
 */

@Composable
fun PaymentStatusScreen(
    viewModel: PaymentStatusViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val status = viewModel.status.value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painterResource(status.statusIcon), "Payment Status",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = context.getString(status.message),
            style = MaterialTheme.typography.body1
        )
    }

    BackHandler(enabled = true) {
        (context as? Activity)?.finish()
    }
}