package com.zzang.chongdae.presentation.view.offeringdetail

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.R
import com.zzang.chongdae.common.firebase.FirebaseAnalyticsManager
import com.zzang.chongdae.databinding.DialogDeleteOfferingBinding
import com.zzang.chongdae.databinding.FragmentOfferingDetailBinding
import com.zzang.chongdae.presentation.view.MainActivity
import com.zzang.chongdae.presentation.view.commentdetail.CommentDetailActivity
import com.zzang.chongdae.presentation.view.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfferingDetailFragment : Fragment(), OnOfferingDeleteClickListener {
    private var _binding: FragmentOfferingDetailBinding? = null
    private val binding get() = _binding!!
    private var toast: Toast? = null
    private val offeringId by lazy {
        arguments?.getLong(HomeFragment.OFFERING_ID) ?: throw IllegalArgumentException()
    }
    private val dialog: Dialog by lazy { Dialog(requireContext()) }

    @Inject
    lateinit var offeringDetailAssistedFactory: OfferingDetailViewModel.OfferingDetailAssistedFactory

    private val viewModel: OfferingDetailViewModel by viewModels {
        OfferingDetailViewModel.getFactory(
            assistedFactory = offeringDetailAssistedFactory,
            offeringId = offeringId,
        )
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpMoveCommentDetailEventObserve()
        (activity as MainActivity).hideBottomNavigation()

        setUpObserve()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadOffering()
    }

    private fun setUpObserve() {
        viewModel.updatedOfferingId.observe(viewLifecycleOwner) {
            setFragmentResult(OFFERING_DETAIL_BUNDLE_KEY, bundleOf(UPDATED_OFFERING_ID_KEY to it))
        }

        viewModel.reportEvent.observe(viewLifecycleOwner) { reportUrlId ->
            openUrlInBrowser(getString(reportUrlId))
        }

        viewModel.error.observe(viewLifecycleOwner) { errMsgId ->
            showToast(errMsgId)
        }

        viewModel.productLinkRedirectEvent.observe(viewLifecycleOwner) { productURL ->
            openUrlInBrowser(productURL)
        }

        viewModel.modifyOfferingEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_offering_detail_fragment_to_offering_modify_essential_fragment,
                bundleOf(HomeFragment.OFFERING_ID to offeringId),
            )
        }

        viewModel.deleteOfferingEvent.observe(viewLifecycleOwner) {
            showUpdateStatusDialog()
        }

        viewModel.deleteOfferingSuccessEvent.observe(viewLifecycleOwner) {
            dialog.dismiss()
            findNavController().popBackStack()
            setFragmentResult(OFFERING_DETAIL_BUNDLE_KEY, bundleOf(DELETED_OFFERING_ID_KEY to true))
        }
    }

    override fun onClickConfirm() {
        viewModel.deleteOffering(offeringId)
        firebaseAnalyticsManager.logSelectContentEvent(
            id = "delete_offering_event",
            name = "delete_offering_event",
            contentType = "button",
        )
    }

    override fun onClickCancel() {
        firebaseAnalyticsManager.logSelectContentEvent(
            id = "cancel_delete_offering_event",
            name = "cancel_delete_offering_event",
            contentType = "button",
        )
        dialog.dismiss()
    }

    private fun showUpdateStatusDialog() {
        val dialogBinding =
            DataBindingUtil.inflate<DialogDeleteOfferingBinding>(
                layoutInflater,
                R.layout.dialog_delete_offering,
                null,
                false,
            )

        dialogBinding.listener = this

        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    private fun openUrlInBrowser(url: String) {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentOfferingDetailBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun setUpMoveCommentDetailEventObserve() {
        viewModel.commentDetailEvent.observe(this) {
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "Offering_Item_ID: $offeringId",
                name = "participate_offering_event",
                contentType = "button",
            )
            findNavController().popBackStack()
            CommentDetailActivity.startActivity(requireContext(), offeringId)
        }
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

    companion object {
        const val OFFERING_DETAIL_BUNDLE_KEY = "offering_detail_bundle_key"
        const val UPDATED_OFFERING_ID_KEY = "updated_offering_id"
        const val DELETED_OFFERING_ID_KEY = "deleted_offering_id"
        const val OFFERING_ID_KEY = "offering_id"
    }
}
