package com.zzang.chongdae.presentation.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.R
import com.zzang.chongdae.presentation.util.FirebaseAnalyticsManager

class MyPageFragment : Fragment() {
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by lazy {
        FirebaseAnalyticsManager(firebaseAnalytics)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalyticsManager.logScreenView(
            screenName = "MyPageFragment",
            screenClass = this::class.java.simpleName,
        )
    }
}
