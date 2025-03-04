package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.presentation.view.home.OfferingViewModel
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

@Composable
internal fun HomeScreen(
    viewModel: OfferingViewModel,
    onOfferingClick: OnOfferingClickListener,
) {
    val offeringSearchState = rememberOfferingSearchState(
        initialText = "",
        placeholder = stringResource(R.string.home_search_hint_text),
        onTextChange = { viewModel.updateSearch(it) }
    )

    val filters by viewModel.filters.observeAsState(emptyList())
    val offeringFiltersState =
        rememberSavableOfferingFiltersState(
            filters = filters,
            onFilterClick = { filter, isChecked ->
                viewModel.onClickFilter(filter.name, isChecked)
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

            LazyColumn {
                items(count = offerings.itemCount) { index ->
                    val item = offerings[index] ?: return@items
                    OfferingItem(item, onOfferingClick)
                    if (index != offerings.itemCount - 1) {
                        HorizontalDivider()
                    }
                }

            }
        }
    }
}

