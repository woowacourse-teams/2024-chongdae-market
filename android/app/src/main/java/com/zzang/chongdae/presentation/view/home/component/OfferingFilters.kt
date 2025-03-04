package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType

@Composable
internal fun OfferingFilters(
    state: OfferingFiltersState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        for (filter in state.filters) {
            if (filter.type == FilterType.INVISIBLE) continue
            OfferingFilter(filter, state)
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
private fun OfferingFilter(
    filter: Filter,
    state: OfferingFiltersState,
) {
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .border(
                BorderStroke(
                    0.5.dp,
                    color = if (state.isSelectedFilter(filter)) {
                        colorResource(id = R.color.main_color)
                    } else colorResource(id = R.color.gray_300),
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .background(color = Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { state.selectFilter(filter) }
            )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .height(24.dp)
                    .padding(start = 6.dp),
                painter = if (state.isSelectedFilter(filter)) {
                    painterResource(id = R.drawable.ic_main_checked_circle)
                } else painterResource(id = R.drawable.ic_main_unchecked_circle),
                contentDescription = "check"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier.padding(end = 15.dp),
                text = filter.value,
                fontSize = with(density) { 14.dp.toSp() },
                color = colorResource(R.color.gray_900),
                fontFamily = FontFamily(
                    Font(
                        R.font.suit_regular,
                        weight = FontWeight.Medium,
                    )
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OfferingFiltersPreview() {
    val filters = listOf(
        Filter(FilterName.JOINABLE, "참여가능만", FilterType.VISIBLE),
        Filter(FilterName.IMMINENT, "마감임박만", FilterType.VISIBLE),
        Filter(FilterName.HIGH_DISCOUNT, "높은할인율순", FilterType.VISIBLE)
    )
    var state = rememberSavableOfferingFiltersState(filters) { _, _ -> }
    OfferingFilters(state)
}
