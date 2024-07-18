package com.zzang.chongdae.domain.model

data class CurrentCount(var currentCount: Int) {
    fun addCurrentCount() {
        currentCount++
    }
}
