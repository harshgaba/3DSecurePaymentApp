package com.example.a3dsecurepaymentapp.presentation.card_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3dsecurepaymentapp.common.Resource
import com.example.a3dsecurepaymentapp.domain.model.CardDetails
import com.example.a3dsecurepaymentapp.domain.use_case.initiate_payment.InitiatePaymentUseCase
import com.example.a3dsecurepaymentapp.presentation.card_details.components.text_field.InputWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatAmexCard
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatDate
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatDinnersClubCard
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatOtherCards
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.CardScheme
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.IdentifyCardScheme
import com.example.a3dsecurepaymentapp.presentation.card_details.CardDetailsConstants.Companion.CREDIT_CARD_NUMBER
import com.example.a3dsecurepaymentapp.presentation.card_details.CardDetailsConstants.Companion.CVV
import com.example.a3dsecurepaymentapp.presentation.card_details.CardDetailsConstants.Companion.EXPIRY_DATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val initiatePayment: InitiatePaymentUseCase,
    private val identifyCardScheme: IdentifyCardScheme,
    private val formatAmexCard: FormatAmexCard,
    private val formatDinnersClubCard: FormatDinnersClubCard,
    private val formatOtherCards: FormatOtherCards,
    private val formatDate: FormatDate,
    private val handle: SavedStateHandle
) : ViewModel() {

    val creditCardNumber = handle.getStateFlow(CREDIT_CARD_NUMBER, InputWrapper())
    val cvv = handle.getStateFlow(CVV, InputWrapper())
    val expiryDate = handle.getStateFlow(EXPIRY_DATE, InputWrapper())

    private val _paymentStatus = Channel<CardDetailState>()
    val paymentStatus = _paymentStatus.receiveAsFlow()

    private val _LoadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _LoadingState


    private val _events = Channel<ScreenEvent>()
    val events = _events.receiveAsFlow()

    private val inputEvents = Channel<UserInputEvent>(Channel.CONFLATED)

    val areInputsValid = combine(cvv, creditCardNumber, expiryDate) { cvv, cardNumber, expiryDate ->
        cvv.value.isNotEmpty() && cvv.errorId == null &&
                cardNumber.value.isNotEmpty() && cardNumber.errorId == null &&
                expiryDate.value.isNotEmpty() && expiryDate.errorId == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    private var focusedTextField =
        handle["focusedTextField"] ?: FocusedTextFieldKey.CREDIT_CARD_NUMBER
        set(value) {
            field = value
            handle["focusedTextField"] = value
        }


    init {
        observeUserInputEvents()
        if (focusedTextField != FocusedTextFieldKey.NONE) focusOnLastSelectedTextField()
    }

    private fun observeUserInputEvents() {
        viewModelScope.launch(Dispatchers.Default) {
            inputEvents.receiveAsFlow()
                .onEach { event ->
                    when (event) {
                        is UserInputEvent.Cvv -> {
                            when (InputValidator.getCVVErrorResIdOrNull(
                                event.input,
                                getCardScheme(event.input)
                            )) {
                                null -> {
                                    handle[CVV] =
                                        cvv.value.copy(value = event.input, errorId = null)
                                }
                                else -> {
                                    handle[CVV] = cvv.value.copy(value = event.input)
                                }
                            }
                        }
                        is UserInputEvent.CreditCard -> {
                            when (InputValidator.getCardNumberErrorResIdOrNull(
                                event.input,
                                getCardScheme(event.input)
                            )) {
                                null -> {
                                    handle[CREDIT_CARD_NUMBER] = creditCardNumber.value.copy(
                                        value = event.input,
                                        errorId = null
                                    )
                                }
                                else -> {
                                    handle[CREDIT_CARD_NUMBER] =
                                        creditCardNumber.value.copy(value = event.input)
                                }
                            }
                        }
                        is UserInputEvent.ExpiryDate -> {
                            when (InputValidator.getExpiryDateErrorResIdOrNull(event.input)) {
                                null -> {
                                    handle[EXPIRY_DATE] =
                                        expiryDate.value.copy(value = event.input, errorId = null)
                                }
                                else -> {
                                    handle[EXPIRY_DATE] = expiryDate.value.copy(value = event.input)
                                }
                            }
                        }
                    }
                }
                .debounce(350)
                .collect { event ->
                    when (event) {
                        is UserInputEvent.Cvv -> {
                            val errorId = InputValidator.getCVVErrorResIdOrNull(
                                event.input,
                                getCardScheme(event.input)
                            )
                            handle[CVV] = cvv.value.copy(errorId = errorId)
                        }
                        is UserInputEvent.ExpiryDate -> {
                            val errorId = InputValidator.getExpiryDateErrorResIdOrNull(event.input)
                            handle[EXPIRY_DATE] = expiryDate.value.copy(errorId = errorId)
                        }
                        is UserInputEvent.CreditCard -> {
                            val errorId = InputValidator.getCardNumberErrorResIdOrNull(
                                event.input,
                                getCardScheme(event.input)
                            )
                            handle[CREDIT_CARD_NUMBER] =
                                creditCardNumber.value.copy(errorId = errorId)
                        }
                    }
                }
        }
    }


    fun onCVVEntered(input: String) {
        inputEvents.trySend(UserInputEvent.Cvv(input))
    }

    fun onExpiryDateEntered(input: String) {
        inputEvents.trySend(UserInputEvent.ExpiryDate(input))
    }

    fun onCardNumberEntered(input: String) {
        inputEvents.trySend(UserInputEvent.CreditCard(input))
    }

    fun onTextFieldFocusChanged(key: FocusedTextFieldKey, isFocused: Boolean) {
        focusedTextField = if (isFocused) key else FocusedTextFieldKey.NONE
    }

    fun onCVVImeActionClick() {
        _events.trySend(ScreenEvent.MoveFocus(FocusDirection.Right))
    }

    fun onExpiryDateImeActionClick() {
        _events.trySend(ScreenEvent.MoveFocus())
    }

    fun onPayClick() {
        viewModelScope.launch(Dispatchers.Default) {
            when (val inputErrors = getInputErrorsOrNull(
                cvv.value.value,
                creditCardNumber.value.value,
                expiryDate.value.value
            )) {
                null -> {
                    clearFocusAndHideKeyboard()
                    makePayment(buildCardDetails())
                }
                else -> displayInputErrors(inputErrors)
            }
        }
    }

    private fun buildCardDetails(): CardDetails {

        return CardDetails(
            expiryYear = expiryDate.value.value.takeLast(4),
            expiryMonth = expiryDate.value.value.substring(0..1),
            number = creditCardNumber.value.value.filter { it.isDigit() },
            cvv = cvv.value.value
        )
    }

    fun getInputErrorsOrNull(cvv: String, creditCard: String, expiryDate: String): InputErrors? {
        val cvvErrorId = InputValidator.getCVVErrorResIdOrNull(cvv, getCardScheme(creditCard))
        val cardErrorId =
            InputValidator.getCardNumberErrorResIdOrNull(creditCard, getCardScheme(creditCard))
        val expiryErrorId = InputValidator.getExpiryDateErrorResIdOrNull(expiryDate)
        return if (cvvErrorId == null && cardErrorId == null && expiryErrorId == null) {
            null
        } else {
            InputErrors(
                cvvErrorId = cvvErrorId,
                cardErrorId = cardErrorId,
                expiryDateErrorId = expiryErrorId
            )
        }
    }

    private fun displayInputErrors(inputErrors: InputErrors) {
        handle[CVV] = cvv.value.copy(errorId = inputErrors.cvvErrorId)
        handle[CREDIT_CARD_NUMBER] = creditCardNumber.value.copy(errorId = inputErrors.cardErrorId)
        handle[EXPIRY_DATE] = expiryDate.value.copy(errorId = inputErrors.expiryDateErrorId)

    }

    private suspend fun clearFocusAndHideKeyboard() {
        _events.send(ScreenEvent.ClearFocus)
        _events.send(ScreenEvent.UpdateKeyboard(false))
        focusedTextField = FocusedTextFieldKey.NONE
    }

    private fun focusOnLastSelectedTextField() {
        viewModelScope.launch(Dispatchers.Default) {
            _events.send(ScreenEvent.RequestFocus(focusedTextField))
            delay(250)
            _events.send(ScreenEvent.UpdateKeyboard(true))
        }
    }

    private fun getCardScheme(cardNumber: String): CardScheme {
        return identifyCardScheme(cardNumber)
    }

    fun getTransformedCardNumber(cardNumber: AnnotatedString): TransformedText {
        return when (getCardScheme(cardNumber.toString())) {
            CardScheme.AMEX -> formatAmexCard(cardNumber)
            CardScheme.DINERS_CLUB -> formatDinnersClubCard(cardNumber)
            else -> formatOtherCards(cardNumber)
        }
    }

    fun getTransformedDate(date: AnnotatedString): TransformedText {
        return formatDate(date)
    }


    private fun makePayment(cardDetails: CardDetails) {
        initiatePayment(cardDetails = cardDetails).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _paymentStatus.send(
                        CardDetailState(payment = result.data)
                    )
                }
                is Resource.Error -> {
                    _paymentStatus.send(
                        CardDetailState(
                            error = result.message ?: "Sorry, Something went wrong!"
                        )
                    )
                }
                is Resource.Loading -> {
                    _LoadingState.value = true
                }
            }
        }.launchIn(viewModelScope)
    }
}


