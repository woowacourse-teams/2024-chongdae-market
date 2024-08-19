package com.zzang.chongdae.presentation.view.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.FragmentHomeBinding
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.presentation.util.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.view.MainActivity
import com.zzang.chongdae.presentation.view.home.adapter.OfferingAdapter
import com.zzang.chongdae.presentation.view.offeringdetail.OfferingDetailFragment
import com.zzang.chongdae.presentation.view.write.OfferingWriteOptionalFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnOfferingClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var offeringAdapter: OfferingAdapter
    private val viewModel: OfferingViewModel by viewModels {
        OfferingViewModel.getFactory(
            offeringRepository = (requireActivity().application as ChongdaeApp).offeringRepository,
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
        initAdapter()
        initSearchListener()
        setUpOfferingsObserve()
        navigateToOfferingWriteFragment()
        initFragmentResultListener()
        setOnCheckboxListener()
    }

    private fun setOnCheckboxListener() {
        binding.cbJoinable.setOnClickListener {
            handleCheckBoxSelection(FilterName.JOINABLE, (it as CheckBox).isChecked)
        }

        binding.cbImminent.setOnClickListener {
            handleCheckBoxSelection(FilterName.IMMINENT, (it as CheckBox).isChecked)
        }

        binding.cbHighDiscount.setOnClickListener {
            handleCheckBoxSelection(FilterName.HIGH_DISCOUNT, (it as CheckBox).isChecked)
        }

        viewModel.selectedFilter.observe(viewLifecycleOwner) { selectedFilter ->
            updateCheckBoxStates(selectedFilter)
        }
    }

    private fun handleCheckBoxSelection(
        filterName: FilterName,
        isChecked: Boolean,
    ) {
        viewModel.onClickFilter(filterName, isChecked)
    }

    private fun updateCheckBoxStates(selectedFilterName: String?) {
        binding.cbJoinable.isChecked = selectedFilterName == FilterName.JOINABLE.name
        binding.cbImminent.isChecked = selectedFilterName == FilterName.IMMINENT.name
        binding.cbHighDiscount.isChecked = selectedFilterName == FilterName.HIGH_DISCOUNT.name
    }

    private fun initFragmentResultListener() {
        setFragmentResultListener(OfferingDetailFragment.OFFERING_DETAIL_BUNDLE_KEY) { _, bundle ->
            viewModel.fetchUpdatedOffering(bundle.getLong(OfferingDetailFragment.UPDATED_OFFERING_ID_KEY))
        }

        setFragmentResultListener(OfferingWriteOptionalFragment.OFFERING_WRITE_BUNDLE_KEY) { _, bundle ->
            viewModel.refreshOfferingsByOfferingWriteEvent(
                bundle.getBoolean(
                    OfferingWriteOptionalFragment.NEW_OFFERING_EVENT_KEY,
                ),
            )
        }
    }

    private fun initSearchListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.onClickSearchButton()
                true
            } else {
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNavigation()
        firebaseAnalyticsManager.logScreenView(
            screenName = "HomeFragment",
            screenClass = this::class.java.simpleName,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun initAdapter() {
        offeringAdapter = OfferingAdapter(this)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                offeringAdapter.loadStateFlow.collect { loadState ->
                    binding.pbLoading.isVisible = loadState.refresh is LoadState.Loading
                }
            }
        }
        binding.rvOfferings.adapter = offeringAdapter
        binding.rvOfferings.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
            ),
        )
    }

    private fun setUpOfferingsObserve() {
        viewModel.offeringsRefreshEvent.observe(viewLifecycleOwner) {
            offeringAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
        }

        viewModel.offerings.observe(viewLifecycleOwner) {
            offeringAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.searchEvent.observe(viewLifecycleOwner) {
            offeringAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
            offeringAdapter.setSearchKeyword(it)
        }

        viewModel.filterOfferingsEvent.observe(viewLifecycleOwner) {
            offeringAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "filter_offerings_event",
                name = "filter_offerings_event",
                contentType = "checkbox",
            )
        }

        viewModel.updatedOffering.observe(viewLifecycleOwner) {
            offeringAdapter.addUpdatedItem(it.toList())
        }
        viewModel.updatedOffering.getValue()?.toList()?.let { offeringAdapter.addUpdatedItem(it) }
    }

    override fun onClick(offeringId: Long) {
        firebaseAnalyticsManager.logSelectContentEvent(
            id = "Offering_Item_ID: $offeringId",
            name = "read_offering_detail_event",
            contentType = "item",
        )

        findNavController().navigate(
            R.id.action_home_fragment_to_offering_detail_fragment,
            Bundle().apply {
                putLong(OFFERING_ID, offeringId)
            },
        )
    }

    private fun navigateToOfferingWriteFragment() {
        binding.fabCreateOffering.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_offering_write_fragment)
        }
    }

    companion object {
        const val OFFERING_ID = "offering_id"
    }
}
