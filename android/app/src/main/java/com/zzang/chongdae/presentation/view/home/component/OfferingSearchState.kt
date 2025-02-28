package com.zzang.chongdae.presentation.view.home.component

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.parcelize.Parcelize

class OfferingSearchState(
    initialText: String = "",
    val placeholder: String? = null,
    val maxLength: Int? = null,
    val validation: (String) -> Boolean = { true },
) {
    var text by mutableStateOf(initialText)
        private set

    val isError: Boolean
        get() = !validation(text)

    val isPlaceHolderVisible: Boolean
        get() = placeholder != null && text.isBlank()

    fun updateText(newText: String) {
        if (isTextLengthExceed(newText, maxLength)) {
            return
        }
        text = newText
    }

    private fun isTextLengthExceed(newText: String, maxLength: Int?) =
        maxLength != null && newText.length > maxLength

    companion object {
        val Saver: Saver<OfferingSearchState, *> = listSaver(
            save = {
                listOf(
                    it.text,
                    it.placeholder,
                    it.maxLength,
                    Validation(it.validation)
                )
            },
            restore = {
                OfferingSearchState(
                    initialText = it[0] as String,
                    placeholder = it[2] as String?,
                    maxLength = it[3] as Int?,
                    validation = (it[4] as Validation).validation,
                )
            }
        )
    }
}

@Parcelize
class Validation(val validation: (String) -> Boolean) : Parcelable

@Composable
fun rememberOfferingSearchState(
    initialText: String = "",
    placeholder: String? = null,
    maxLength: Int? = null,
    validation: (String) -> Boolean = { true },
): OfferingSearchState {
    return rememberSaveable(saver = OfferingSearchState.Saver) {
        OfferingSearchState(
            initialText,
            placeholder,
            maxLength,
            validation
        )
    }
}
