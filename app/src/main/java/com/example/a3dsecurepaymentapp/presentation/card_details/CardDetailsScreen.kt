package com.example.a3dsecurepaymentapp.presentation.card_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.example.a3dsecurepaymentapp.presentation.card_details.components.text_field.CustomTextField
import com.example.a3dsecurepaymentapp.R

@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun CardDetailsScreen(
    navController: NavController,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    val cvv by viewModel.cvv.collectAsState()
    val expiryDate by viewModel.expiryDate.collectAsState()
    val creditCardNumber by viewModel.creditCardNumber.collectAsState()
    val areInputsValid by viewModel.areInputsValid.collectAsState()

    val creditCardNumberFocusRequester = remember { FocusRequester() }
    val cvvFocusRequester = remember { FocusRequester() }
    val expiryFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ScreenEvent.UpdateKeyboard -> {
                    if (event.show) keyboardController?.show() else keyboardController?.hide()
                }
                is ScreenEvent.ClearFocus -> focusManager.clearFocus()
                is ScreenEvent.RequestFocus -> {
                    when (event.textFieldKey) {
                        FocusedTextFieldKey.CVV -> cvvFocusRequester.requestFocus()
                        FocusedTextFieldKey.CREDIT_CARD_NUMBER -> creditCardNumberFocusRequester.requestFocus()
                        FocusedTextFieldKey.EXPIRY_DATE -> expiryFocusRequester.requestFocus()
                        else -> {}
                    }
                }
                is ScreenEvent.MoveFocus -> focusManager.moveFocus(event.direction)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 200.dp, bottom = 50.dp, end = 16.dp),
    ) {

        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(creditCardNumberFocusRequester)
                .onFocusChanged { focusState ->
                    viewModel.onTextFieldFocusChanged(
                        key = FocusedTextFieldKey.CREDIT_CARD_NUMBER,
                        isFocused = focusState.isFocused
                    )
                },
            labelResId = R.string.credit_card_number,
            keyboardOptions = remember {
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            },
            visualTransformation = viewModel::getTransformedCardNumber,
            inputWrapper = creditCardNumber,
            onValueChange = viewModel::onCardNumberEntered,
            onImeKeyAction = viewModel::onExpiryDateImeActionClick
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .focusRequester(expiryFocusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.onTextFieldFocusChanged(
                            key = FocusedTextFieldKey.EXPIRY_DATE,
                            isFocused = focusState.isFocused
                        )
                    },
                placeHolder="02/2033",
                labelResId = R.string.expiry_date,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                },
                visualTransformation = viewModel::getTransformedDate,
                inputWrapper = expiryDate,
                onValueChange = viewModel::onExpiryDateEntered,
                onImeKeyAction = viewModel::onCVVImeActionClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            CustomTextField(
                modifier = Modifier
                    .focusRequester(cvvFocusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.onTextFieldFocusChanged(
                            key = FocusedTextFieldKey.CVV,
                            isFocused = focusState.isFocused
                        )
                    },
                labelResId = R.string.cvv,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                },
                inputWrapper = cvv,
                onValueChange = viewModel::onCVVEntered,
                onImeKeyAction = viewModel::onPayClick
            )
        }
        Spacer(Modifier.height(48.dp))
        Button(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            onClick = viewModel::onPayClick, enabled = areInputsValid
        ) {
            Text(context.getString(R.string.pay))
        }
    }
}