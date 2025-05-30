package com.zzang.chongdae.presentation.view.nickname.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzang.chongdae.presentation.ui.theme.Gray300
import com.zzang.chongdae.presentation.ui.theme.Gray500
import com.zzang.chongdae.presentation.ui.theme.MainColor
import com.zzang.chongdae.presentation.ui.theme.Typography
import com.zzang.chongdae.presentation.ui.theme.White

@Composable
fun PrimaryActionButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier =
            modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = if (enabled) MainColor else Gray300,
                contentColor = White,
                disabledContainerColor = Gray500,
                disabledContentColor = White,
            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = text,
            style = Typography.titleMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryActionButtonPreview() {
    Column {
        PrimaryActionButton(
            text = "변경하기",
            enabled = false,
            onClick = {},
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryActionButton(
            text = "변경하기",
            enabled = true,
            onClick = {},
        )
    }
}
