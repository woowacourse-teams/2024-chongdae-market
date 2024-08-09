package com.zzang.chongdae.presentation.view.address

import android.os.Looper

class JavascriptInterface(private val onAddressClickListener: OnAddressClickListener) {
    @android.webkit.JavascriptInterface
    fun result(address: String) {
        android.os.Handler(Looper.getMainLooper()).post {
            onAddressClickListener.onClickAddress(address)
        }
    }
}
