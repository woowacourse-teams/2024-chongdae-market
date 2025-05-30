package com.zzang.chongdae.presentation.view.nickname.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzang.chongdae.presentation.ui.theme.AppGray600
import com.zzang.chongdae.presentation.ui.theme.DividerGray
import com.zzang.chongdae.presentation.ui.theme.Typography

@Composable
fun EditNicknameTopBar(
    modifier: Modifier = Modifier,
    titleText: String,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(60.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = titleText,
            style = Typography.headlineMedium,
            color = AppGray600(),
        )
        HorizontalDivider(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            thickness = 3.dp,
            color = DividerGray,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditNicknameTopBarPreview() {
    EditNicknameTopBar(titleText = "닉네임 변경")
}
