package com.zzang.chongdae.presentation.view.home

import com.zzang.chongdae.domain.model.FilterName

interface OnFilterClickListener {
    fun onClickFilter(
        filterName: FilterName,
        isChecked: Boolean,
    )
}
