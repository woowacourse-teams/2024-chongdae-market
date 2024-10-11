package com.zzang.chongdae.presentation.view.write

import androidx.annotation.StringRes

sealed class WriteUIState {
    data class Empty(
        @StringRes val message: Int,
    ) : WriteUIState()

    data object Initial : WriteUIState()

    data object Loading : WriteUIState()

    data class InvalidInput(
        @StringRes val message: Int,
    ) : WriteUIState()

    data class Success(val url: String) : WriteUIState()

    data class Error(
        @StringRes val message: Int,
        val errorMessage: String,
    ) : WriteUIState()
}
