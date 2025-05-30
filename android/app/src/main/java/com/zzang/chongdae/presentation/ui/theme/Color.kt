package com.zzang.chongdae.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF3B3B3B)
val White = Color(0xFFFFFFFF)
val MainColor = Color(0xFFF15642)

val DividerGray = Color(0xFFDDDDDD)
val GrayFont = Color(0xFF7A7A7A)
val Gray100 = Color(0xFFF8F8FA)
val Gray200 = Color(0xFFEDEDED)
val Gray300 = Color(0xFFD9D9D9)
val Gray400 = Color(0xFFBABABA)
val Gray500 = Color(0xFF7A7A7A)
val Gray600 = Color(0xFF606060)
val Gray700 = Color(0xFF697C7A)
val Gray900 = Color(0xFF3F3F3F)

val ButtonGreen = Color(0x67C451)

@Composable
fun AppWhite(): Color {
    return if (isSystemInDarkTheme()) Black else White
}

@Composable
fun AppBlack(): Color {
    return if (isSystemInDarkTheme()) White else Black
}

@Composable
fun AppGray600(): Color {
    return if (isSystemInDarkTheme()) White else Gray600
}
