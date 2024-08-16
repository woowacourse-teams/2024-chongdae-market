package com.zzang.chongdae.presentation.view.write

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.FragmentOfferingWriteOptionalBinding
import com.zzang.chongdae.presentation.util.FileUtils
import com.zzang.chongdae.presentation.util.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.util.PermissionManager

class OfferingWriteOptionalFragment : Fragment() {
    private var _fragmentBinding: FragmentOfferingWriteOptionalBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    private var toast: Toast? = null

    private lateinit var permissionManager: PermissionManager
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel: OfferingWriteViewModel by activityViewModels {
        OfferingWriteViewModel.getFactory(
            offeringRepository = (requireActivity().application as ChongdaeApp).offeringRepository,
        )
    }

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
        _fragmentBinding = FragmentOfferingWriteOptionalBinding.inflate(inflater, container, false)
        fragmentBinding.vm = viewModel
        fragmentBinding.lifecycleOwner = viewLifecycleOwner
    }

    private fun observeSubmitOfferingEvent() {
        viewModel.submitOfferingEvent.observe(viewLifecycleOwner) {
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "submit_offering_event",
                name = "submit_offering_event",
                contentType = "button",
            )
            showToast(R.string.write_success_writing)
            findNavController().popBackStack(R.id.offering_write_fragment_essential, true)
            viewModel.initOfferingWriteInputs()
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
        viewModel.writeUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WriteUIState.Error -> {
                    showToast(state.message)
                }
                is WriteUIState.Empty -> {
                    showToast(state.message)
                }
                is WriteUIState.InvalidInput -> {
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
}
