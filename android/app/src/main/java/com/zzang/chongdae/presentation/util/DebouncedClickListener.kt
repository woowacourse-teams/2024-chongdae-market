package com.zzang.chongdae.presentation.util

import android.os.SystemClock
import android.view.View

fun View.setDebouncedOnClickListener(
    debounceTime: Long = DEFAULT_DEBOUNCED_TIME,
    action: (View) -> Unit,
) {
    var lastClickTime = 0L

    this.setDebouncedOnClickListener {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            action(it)
        }
    }
}

const val DEFAULT_DEBOUNCED_TIME = 200L