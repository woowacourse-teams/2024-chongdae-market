package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class OfferingSearchState(
    initialText: String = "",
    val placeholder: String? = null,
    val maxLength: Int? = null,
    private val onTextChange: (String) -> Unit = {},
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
        onTextChange(newText)
    }

    private fun isTextLengthExceed(
        newText: String,
        maxLength: Int?,
    ) = maxLength != null && newText.length > maxLength

    companion object {
        fun saver(onTextChange: (String) -> Unit = {}): Saver<OfferingSearchState, *> =
            Saver(
                save =
                    {
                        listOf(
                            it.text,
                            it.placeholder,
                            it.maxLength,
                        )
                    },
                restore =
                    {
                        OfferingSearchState(
                            initialText = it[0] as String,
                            placeholder = it[1] as String?,
                            maxLength = it[2] as Int?,
                            onTextChange = onTextChange,
                        )
                    },
            )
    }
}

@Composable
fun rememberOfferingSearchState(
    initialText: String = "",
    placeholder: String? = null,
    maxLength: Int? = null,
    onTextChange: (String) -> Unit = {},
): OfferingSearchState {
    return rememberSaveable(
        initialText,
        saver = OfferingSearchState.saver(onTextChange),
    ) {
        OfferingSearchState(
            initialText,
            placeholder,
            maxLength,
            onTextChange,
        )
    }
}
