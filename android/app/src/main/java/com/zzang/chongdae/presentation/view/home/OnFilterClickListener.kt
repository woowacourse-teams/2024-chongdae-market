package com.zzang.chongdae.presentation.view.home

import com.zzang.chongdae.domain.model.Filter

interface OnFilterClickListener {
    fun onClickFilter(
        filter: Filter,
        isChecked: Boolean,
    )
}
