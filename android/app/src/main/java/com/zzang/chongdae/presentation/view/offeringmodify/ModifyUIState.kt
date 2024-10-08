package com.zzang.chongdae.presentation.view.offeringmodify

import androidx.annotation.StringRes

sealed class ModifyUIState {
    data class Empty(
        @StringRes val message: Int,
    ) : ModifyUIState()

    data object Initial : ModifyUIState()

    data object Loading : ModifyUIState()

    data class InvalidInput(
        @StringRes val message: Int,
    ) : ModifyUIState()

    data class Success(val url: String) : ModifyUIState()

    data class Error(
        @StringRes val message: Int,
        val errorMessage: String,
    ) : ModifyUIState()
}
