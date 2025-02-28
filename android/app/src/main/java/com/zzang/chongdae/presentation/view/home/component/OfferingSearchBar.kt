package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzang.chongdae.R

@Composable
internal fun OfferingSearchBar(
    state: OfferingSearchState,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit,
) {
    val density = LocalDensity.current

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        value = state.text,
        onValueChange = { state.updateText(it) },
        textStyle = TextStyle(
            fontSize = with(density) { 15.dp.toSp()},
            fontFamily = FontFamily(
                Font(
                    R.font.suit_medium,
                    weight = FontWeight.Medium,
                )
            )
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
        ),
    ) { innerTextField ->
        Column {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    innerTextField()
                    if (state.isPlaceHolderVisible) {
                        Text(
                            text = state.placeholder ?: return@Box,
                            maxLines = 1,
                            style = TextStyle(
                                fontSize = with(density) { 15.dp.toSp()},
                                color = Color.Gray,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.suit_medium,
                                        weight = FontWeight.Medium,
                                    )
                                )
                            ),
                        )
                    }
                }

                if (state.text.isNotBlank()) {
                    IconButton(
                        modifier = Modifier
                            .size(15.dp),
                        onClick = onClearClick
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.btn_offering_write_clear),
                            contentDescription = "Clear Search"
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                }
                IconButton(
                    modifier = Modifier.size(25.dp),
                    onClick = onSearchClick
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_main_search),
                        contentDescription = "Search"
                    )
                }
            }
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(color = colorResource(id = R.color.main_color)),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OfferingSearchBarPreview() {
    val state =
        rememberOfferingSearchState(
            initialText = "1",
            placeholder = stringResource(R.string.home_search_hint_text)
        )
    MaterialTheme {
        OfferingSearchBar(state, {}, { state.updateText("") })
    }
}
