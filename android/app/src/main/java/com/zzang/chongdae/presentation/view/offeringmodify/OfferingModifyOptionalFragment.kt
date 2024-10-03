package com.zzang.chongdae.presentation.view.offeringmodify

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.R
import com.zzang.chongdae.common.firebase.FirebaseAnalyticsManager
import com.zzang.chongdae.databinding.FragmentOfferingModifyOptionalBinding
import com.zzang.chongdae.presentation.util.FileUtils
import com.zzang.chongdae.presentation.util.PermissionManager

class OfferingModifyOptionalFragment : Fragment() {
    private var _fragmentBinding: FragmentOfferingModifyOptionalBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    private var toast: Toast? = null

    private lateinit var permissionManager: PermissionManager
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel: OfferingModifyViewModel by activityViewModels()

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by lazy {
        FirebaseAnalyticsManager(firebaseAnalytics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpPermissionManager()
        initializePhotoPicker()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initBinding(inflater, container)
        return fragmentBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserve()
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _fragmentBinding = FragmentOfferingModifyOptionalBinding.inflate(inflater, container, false)
        fragmentBinding.vm = viewModel
        fragmentBinding.lifecycleOwner = viewLifecycleOwner
    }

    private fun observeSubmitOfferingEvent() {
        viewModel.submitOfferingModifyEvent.observe(viewLifecycleOwner) {
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "submit_offering_event",
                name = "submit_offering_event",
                contentType = "button",
            )
            showToast(R.string.modify_success_modifing)
            findNavController().popBackStack(R.id.offering_modify_essential_fragment, true)
            viewModel.initOfferingModifyInputs()

            setFragmentResult(
                OFFERING_WRITE_BUNDLE_KEY,
                bundleOf(NEW_OFFERING_EVENT_KEY to true),
            )
        }
    }

    private fun setUpObserve() {
        observeUIState()
        observeSubmitOfferingEvent()
        observeImageUploadEvent()
    }

    private fun showToast(
        @StringRes messageId: Int,
    ) {
        toast?.cancel()
        toast =
            Toast.makeText(
                requireActivity(),
                getString(messageId),
                Toast.LENGTH_SHORT,
            )
        toast?.show()
    }

    private fun initializePhotoPicker() {
        pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                handleMediaResult(uri)
            }
    }

    private fun launchPhotoPicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handleMediaResult(uri: Uri?) {
        if (uri != null) {
            val multipartBodyPart = FileUtils.getMultipartBodyPart(requireContext(), uri, "image")
            if (multipartBodyPart != null) {
                viewModel.uploadImageFile(multipartBodyPart)
            } else {
                showToast(R.string.all_error_file_conversion)
            }
        }
    }

    private fun setUpPermissionManager() {
        permissionManager =
            PermissionManager(
                fragment = this,
                onPermissionGranted = { onPermissionsGranted() },
                onPermissionDenied = { onPermissionsDenied() },
            )
    }

    private fun observeImageUploadEvent() {
        viewModel.imageUploadEvent.observe(viewLifecycleOwner) {
            if (permissionManager.isAndroid13OrAbove()) {
                launchPhotoPicker()
            } else {
                permissionManager.requestPermissions()
            }
        }
    }

    private fun observeUIState() {
        viewModel.modifyUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModifyUIState.Error -> {
                    showToast(state.message)
                }

                is ModifyUIState.Empty -> {
                    showToast(state.message)
                }

                is ModifyUIState.InvalidInput -> {
                    showToast(state.message)
                }

                else -> {}
            }
        }
    }

    private fun onPermissionsGranted() {
        showToast(R.string.all_permission_granted)
        launchPhotoPicker()
    }

    private fun onPermissionsDenied() {
        showToast(R.string.all_permission_denied)
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentBinding = null
    }

    companion object {
        const val OFFERING_WRITE_BUNDLE_KEY = "offering_write_bundle_key"
        const val NEW_OFFERING_EVENT_KEY = "new_offering_event_key"
    }
}
