package com.zzang.chongdae.presentation.view.address

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.webkit.WebViewAssetLoader
import com.zzang.chongdae.databinding.DialogAddressFinderBinding

class AddressFinderDialog : DialogFragment(), OnAddressClickListener {
    private var _binding: DialogAddressFinderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddressFinderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()
        initWebView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDialog() {
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val assetLoader = webViewAssetLoader()

        binding.wvAddress.run {
            with(settings) {
                javaScriptEnabled = true
                allowFileAccess = false
                allowContentAccess = false
            }
            addJavascriptInterface(
                JavascriptInterface(this@AddressFinderDialog),
                JS_BRIDGE
            )
            webViewClient = AddressWebViewClient(assetLoader)
            loadUrl("https://${DOMAIN}/${PATH}/html/address.html")
        }
    }

    private fun webViewAssetLoader() = WebViewAssetLoader.Builder()
        .addPathHandler(
            "/${PATH}/",
            WebViewAssetLoader.AssetsPathHandler(requireContext())
        )
        .setDomain(DOMAIN)
        .build()

    override fun onClickAddress(address: String) {
        setFragmentResult(ADDRESS_KEY, bundleOf(BUNDLE_ADDRESS_KEY to address))
        dismiss()
    }

    companion object {
        private const val JS_BRIDGE = "address_finder"
        private const val DOMAIN = "address.finder.net"
        private const val PATH = "assets"

        const val ADDRESS_KEY = "address_key"
        const val BUNDLE_ADDRESS_KEY = "address"
    }
}
