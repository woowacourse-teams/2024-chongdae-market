package com.zzang.chongdae.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography =
    Typography(
        // 사용 o
        headlineMedium =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
            ),
        // 사용 x
        bodyLarge =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = SuitFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
            ),
    )
