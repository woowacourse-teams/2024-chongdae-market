package com.zzang.chongdae.presentation.view.nickname

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zzang.chongdae.R
import com.zzang.chongdae.presentation.ui.theme.AppBlack
import com.zzang.chongdae.presentation.ui.theme.GrayFont
import com.zzang.chongdae.presentation.ui.theme.MainColor
import com.zzang.chongdae.presentation.ui.theme.Typography
import com.zzang.chongdae.presentation.view.nickname.component.EditNicknameTopBar
import com.zzang.chongdae.presentation.view.nickname.component.PrimaryActionButton
import com.zzang.chongdae.presentation.view.nickname.event.NicknameEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditNicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: EditNicknameViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val nickname by remember { derivedStateOf { viewModel.nickname } }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is NicknameEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is NicknameEvent.NicknameChanged -> {
                    Toast.makeText(context, "닉네임 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    onBack()
                }
                NicknameEvent.InvalidNickname -> Toast.makeText(context, "닉네임이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .imePadding(),
    ) {
        Column(
            modifier =
                Modifier
                    .verticalScroll(scrollState)
                    .padding(bottom = 76.dp)
                    .fillMaxSize(),
        ) {
            EditNicknameTopBar(titleText = stringResource(R.string.my_page_edit_nickname))
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                text = stringResource(R.string.edit_nickname_contents),
                style = Typography.bodyMedium,
                color = AppBlack(),
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                value = nickname,
                onValueChange = { viewModel.onNicknameChange(it) },
                placeholder = {
                    Text(stringResource(R.string.edit_nickname_input_text), style = Typography.bodyMedium, color = GrayFont)
                },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (viewModel.shouldShowHelper) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                    text = stringResource(R.string.edit_nickname_invalid),
                    color = MainColor,
                    style = Typography.bodySmall,
                )
            }
        }

        PrimaryActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 50.dp),
            text = stringResource(R.string.edit_nickname_button_text),
            enabled = viewModel.isNicknameValid,
            onClick = { viewModel.confirmNickname() },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditNicknameScreenPreview() {
    EditNicknameScreen(
        onBack = {},
    )
}
