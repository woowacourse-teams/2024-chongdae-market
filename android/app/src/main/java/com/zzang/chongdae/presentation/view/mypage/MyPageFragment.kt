package com.zzang.chongdae.presentation.view.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp.Companion.dataStore
import com.zzang.chongdae.databinding.FragmentMyPageBinding
import com.zzang.chongdae.local.source.UserPreferencesDataStore
import com.zzang.chongdae.common.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.view.login.LoginActivity

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels {
        MyPageViewModel.getFactory(UserPreferencesDataStore(requireContext().dataStore))
    }

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
    ): View {
        initBinding(inflater, container)
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserve()
    }

    private fun setUpObserve() {
        viewModel.openUrlInBrowserEvent.observe(viewLifecycleOwner) {
            openUrlInBrowser(it)
        }
        viewModel.logoutEvent.observe(viewLifecycleOwner) {
            clearDataAndLogout()
        }
    }

    private fun clearDataAndLogout() {
        LoginActivity.startActivity(requireContext())
    }

    private fun openUrlInBrowser(url: String) {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalyticsManager.logScreenView(
            screenName = "MyPageFragment",
            screenClass = this::class.java.simpleName,
        )
    }
}
