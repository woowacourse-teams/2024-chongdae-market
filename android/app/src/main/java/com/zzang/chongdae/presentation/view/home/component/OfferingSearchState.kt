package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class OfferingSearchState(
    initialText: String = "",
    val placeholder: String? = null,
    val maxLength: Int? = null,
) {
    var text by mutableStateOf(initialText)
        private set

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
                )
            },
            restore = {
                OfferingSearchState(
                    initialText = it[0] as String,
                    placeholder = it[2] as String?,
                    maxLength = it[3] as Int?,
                )
            }
        )
    }
}

@Composable
fun rememberOfferingSearchState(
    initialText: String = "",
    placeholder: String? = null,
    maxLength: Int? = null,
): OfferingSearchState {
    return rememberSaveable(saver = OfferingSearchState.Saver) {
        OfferingSearchState(
            initialText,
            placeholder,
            maxLength,
        )
    }
}
