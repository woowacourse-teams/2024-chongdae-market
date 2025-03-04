package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.presentation.view.home.OfferingViewModel
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    viewModel: OfferingViewModel,
    onOfferingClick: OnOfferingClickListener,
) {
    val density = LocalDensity.current
    val search by viewModel.search.observeAsState()

    val offeringSearchState = rememberOfferingSearchState(
        initialText = search ?: "",
        placeholder = stringResource(R.string.home_search_hint_text),
        onTextChange = { viewModel.updateSearch(it) }
    )

    val filters by viewModel.filters.observeAsState(emptyList())
    val selectedFilter by viewModel.selectedFilter.observeAsState()
    val offeringFiltersState =
        rememberSavableOfferingFiltersState(
            filters = filters,
            initSelectedFilter = selectedFilter,
            onFilterClick = { filter, isChecked ->
                viewModel.onClickFilter(filter, isChecked)
            }
        )

    val offerings = viewModel.offerings.collectAsLazyPagingItems()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            OfferingSearchBar(
                offeringSearchState,
                { viewModel.onClickSearchButton() },
                { viewModel.refreshOfferings(true) })

            OfferingFilters(offeringFiltersState)

            if (offerings.loadState.refresh == LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.CenterVertically),
                )
                return@Surface
            }

            if (offerings.itemCount == 0) {
                Text(
                    text = stringResource(R.string.home_empty_item_comment),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.CenterVertically),
                    fontSize = with(density) { 22.dp.toSp() },
                    fontFamily = FontFamily(
                        Font(
                            R.font.suit_medium,
                            weight = FontWeight.Medium,
                        )
                    )
                )
                return@Surface
            }

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
                    Modifier.fillMaxSize()
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
            }
        }
    }
}

