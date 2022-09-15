package com.example.a3dsecurepaymentapp.presentation.card_details.components.text_field

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InputWrapper(val value: String = "", val errorId: Int? = null) : Parcelable