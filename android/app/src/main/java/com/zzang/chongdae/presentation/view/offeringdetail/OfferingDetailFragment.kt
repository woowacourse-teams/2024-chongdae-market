package com.zzang.chongdae.presentation.view.offeringdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.databinding.FragmentOfferingDetailBinding
import com.zzang.chongdae.presentation.util.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.view.MainActivity
import com.zzang.chongdae.presentation.view.commentdetail.CommentDetailActivity
import com.zzang.chongdae.presentation.view.home.HomeFragment

class OfferingDetailFragment : Fragment() {
    private var _binding: FragmentOfferingDetailBinding? = null
    private val binding get() = _binding!!
    private val offeringId by lazy {
        arguments?.getLong(HomeFragment.OFFERING_ID)
            ?: throw IllegalArgumentException()
    }
    private val viewModel: OfferingDetailViewModel by viewModels {
        OfferingDetailViewModel.getFactory(
            offeringId = offeringId,
            offeringDetailRepository = (requireActivity().application as ChongdaeApp).offeringDetailRepository,
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

        viewModel.updatedOfferingId.observe(viewLifecycleOwner) {
            setFragmentResult(OFFERING_DETAIL_BUNDLE_KEY, bundleOf(UPDATED_OFFERING_ID_KEY to it))
        }
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
        viewModel.commentDetailEvent.observe(this) { offeringTitle ->

            firebaseAnalyticsManager.logSelectContentEvent(
                id = "Offering_Item_ID: $offeringId",
                name = "participate_offering_event",
                contentType = "button",
            )
            CommentDetailActivity.startActivity(requireContext(), offeringId, offeringTitle)
        }
    }

    companion object {
        const val OFFERING_DETAIL_BUNDLE_KEY = "offering_detail_bundle_key"
        const val UPDATED_OFFERING_ID_KEY = "updated_offering_id"
    }
}
