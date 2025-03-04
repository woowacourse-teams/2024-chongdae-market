package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.presentation.view.home.OfferingViewModel
import com.zzang.chongdae.presentation.view.home.OnFloatingClickListener
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun OfferingsList(
    viewModel: OfferingViewModel,
    offerings: LazyPagingItems<Offering>,
    onOfferingClick: OnOfferingClickListener,
    onFloatingClick: OnFloatingClickListener,
) {
    val searchKeyword by viewModel.searchEvent.observeAsState()
    val updatedOfferings by viewModel.updatedOffering.observeAsState(emptyList())
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.swipeRefresh()
            isRefreshing = false
        },
        state = pullToRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = Color.White,
                color = colorResource(R.color.main_color),
                state = pullToRefreshState
            )
        }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
                .padding(horizontal = 30.dp)
        ) {
            items(count = offerings.itemCount) { index ->
                val item = offerings[index] ?: return@items
                val updatedItem = updatedOfferings.firstOrNull { it.id == item.id } ?: item
                OfferingItem(
                    updatedItem,
                    onOfferingClick,
                    searchKeyword ?: "",
                    R.color.search_keyword
                )
                if (index != offerings.itemCount - 1) {
                    HorizontalDivider()
                }
            }
        }

        FloatingActionButton(
            onClick = { onFloatingClick.onClickFloatingButton() },
            shape = CircleShape,
            containerColor = colorResource(R.color.main_color),
            modifier = Modifier
                .padding(end = 20.dp, bottom = 30.dp)
                .align(Alignment.BottomEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.btn_main_create_offering),
                contentDescription = "Floating action button."
            )
        }
    }
}
