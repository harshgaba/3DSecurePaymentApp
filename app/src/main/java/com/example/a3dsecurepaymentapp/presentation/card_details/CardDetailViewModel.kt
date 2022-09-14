package com.example.a3dsecurepaymentapp.presentation.card_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatDinnersClubCard
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.formaters.FormatOtherCards
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.CardScheme
import com.example.a3dsecurepaymentapp.domain.use_case.card_details.scheme.IdentifyCardScheme
import com.example.a3dsecurepaymentapp.presentation.card_details.CardDetailsConstants.Companion.CREDIT_CARD_NUMBER
import com.example.a3dsecurepaymentapp.presentation.card_details.CardDetailsConstants.Companion.CVV
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
    private val handle: SavedStateHandle
) : ViewModel() {

    val creditCardNumber = handle.getStateFlow(CREDIT_CARD_NUMBER, InputWrapper())
    val cvv = handle.getStateFlow(CVV, InputWrapper())

    private val _state = mutableStateOf(CardDetailState())
    val state: State<CardDetailState> = _state

    private val _events = Channel<ScreenEvent>()
    val events = _events.receiveAsFlow()

    private val inputEvents = Channel<UserInputEvent>(Channel.CONFLATED)

    val areInputsValid = combine(cvv, creditCardNumber) { cvv, cardNumber ->
        cvv.value.isNotEmpty() && cvv.errorId == null &&
                cardNumber.value.isNotEmpty() && cardNumber.errorId == null
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
                            when (InputValidator.getCVVErrorResIdOrNull(event.input,getCardScheme(event.input))) {
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
                            when (InputValidator.getCardNumberErrorResIdOrNull(event.input,getCardScheme(event.input))) {
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
                    }
                }
                .debounce(350)
                .collect { event ->
                    when (event) {
                        is UserInputEvent.Cvv -> {
                            val errorId = InputValidator.getCVVErrorResIdOrNull(event.input,getCardScheme(event.input))
                            handle[CVV] = cvv.value.copy(errorId = errorId)
                        }
                        is UserInputEvent.CreditCard -> {
                            val errorId = InputValidator.getCardNumberErrorResIdOrNull(event.input,getCardScheme(event.input))
                            handle[CREDIT_CARD_NUMBER] =
                                creditCardNumber.value.copy(errorId = errorId)
                        }
                    }
                }
        }
    }


    fun getInputErrorsOrNull(cvv: String, creditCard: String): InputErrors? {
        val nameErrorId = InputValidator.getCVVErrorResIdOrNull(cvv,getCardScheme(creditCard))
        val cardErrorId = InputValidator.getCardNumberErrorResIdOrNull(creditCard,getCardScheme(creditCard))
        return if (nameErrorId == null && cardErrorId == null) {
            null
        } else {
            InputErrors(nameErrorId, cardErrorId)
        }
    }

    private fun displayInputErrors(inputErrors: InputErrors) {
        handle[CVV] = cvv.value.copy(errorId = inputErrors.cvvErrorId)
        handle[CREDIT_CARD_NUMBER] = creditCardNumber.value.copy(errorId = inputErrors.cardErrorId)
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
}


