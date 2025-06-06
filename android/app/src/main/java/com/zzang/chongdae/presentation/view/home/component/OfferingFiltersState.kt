package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.zzang.chongdae.domain.model.Filter

class OfferingFiltersState(
    val filters: List<Filter>,
    initSelectedFilter: Filter? = null,
    private val onFilterClick: (Filter, Boolean) -> Unit,
) {
    var selectedFilter by mutableStateOf(initSelectedFilter)
        private set

    fun selectFilter(filter: Filter) {
        selectedFilter =
            if (selectedFilter != filter) {
                filter
            } else {
                null
            }
        onFilterClick(filter, isSelectedFilter(filter))
    }

    fun isSelectedFilter(filter: Filter): Boolean = selectedFilter == filter

    companion object {
        fun saver(
            filters: List<Filter>,
            onFilterClick: (Filter, Boolean) -> Unit,
        ): Saver<OfferingFiltersState, *> =
            Saver(
                save = {
                    it.selectedFilter?.value
                },
                restore = { savedValue ->
                    OfferingFiltersState(
                        initSelectedFilter = filters.find { it.value == savedValue },
                        filters = filters,
                        onFilterClick = onFilterClick,
                    )
                },
            )
    }
}

@Composable
fun rememberSavableOfferingFiltersState(
    filters: List<Filter>,
    initSelectedFilter: Filter? = null,
    onFilterClick: (Filter, Boolean) -> Unit,
): OfferingFiltersState {
    return rememberSaveable(
        initSelectedFilter,
        filters,
        onFilterClick,
        saver = OfferingFiltersState.saver(filters, onFilterClick),
    ) { OfferingFiltersState(filters, initSelectedFilter, onFilterClick) }
}
