package com.zzang.chongdae.presentation.view.home

import android.view.View
import com.zzang.chongdae.domain.model.FilterName

interface OnFilterClickListener {
    /*fun onClickFilter(
        filterName: FilterName,
        isChecked: Boolean,
        button: Button,
    )*/

    fun onClickFilter(
        filterName: FilterName,
        button: View,
    )
}
