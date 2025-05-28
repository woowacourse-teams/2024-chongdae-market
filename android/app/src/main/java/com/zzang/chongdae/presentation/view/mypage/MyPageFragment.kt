package com.zzang.chongdae.presentation.view.mypage

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.R
import com.zzang.chongdae.common.firebase.FirebaseAnalyticsManager
import com.zzang.chongdae.databinding.DialogAlertBinding
import com.zzang.chongdae.databinding.FragmentMyPageBinding
import com.zzang.chongdae.presentation.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private var _alertBinding: DialogAlertBinding? = null
    private val alertBinding get() = _alertBinding!!

    private val alert: Dialog by lazy { Dialog(requireContext()) }

    private val viewModel: MyPageViewModel by viewModels()

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

        _alertBinding = DialogAlertBinding.inflate(inflater, container, false)
        alertBinding.listener = viewModel
        alertBinding.tvDialogMessage.text = getString(R.string.my_page_logout_dialog_description)
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
        viewModel.showAlertEvent.observe(viewLifecycleOwner) {
            alert.setContentView(alertBinding.root)
            alert.show()
        }
        viewModel.alertCancelEvent.observe(viewLifecycleOwner) {
            alert.dismiss()
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
        updateNoficationStatus()
    }

    private fun updateNoficationStatus() {
        val areNotificationEnabled =
            NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        viewModel.updateNotificationStatus(areNotificationEnabled)
    }
}
