package com.zzang.chongdae.presentation.view.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.FragmentHomeBinding
import com.zzang.chongdae.presentation.view.MainActivity
import com.zzang.chongdae.presentation.view.home.adapter.OfferingAdapter
import com.zzang.chongdae.presentation.view.offeringdetail.OfferingDetailActivity

class HomeFragment : Fragment(), OnOfferingClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var offeringAdapter: OfferingAdapter
    private val viewModel: OfferingViewModel by viewModels {
        OfferingViewModel.getFactory(
            offeringRepository = (requireActivity().application as ChongdaeApp).offeringRepository,
        )
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
        setUpOfferingsObserve()
        navigateToOfferingWriteFragment()
    }

    override fun onStart() {
        super.onStart()

        offeringAdapter.refresh()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNavigation()
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
        scrollToTopWhenLoadingFinish()
        binding.rvOfferings.adapter = offeringAdapter
        binding.rvOfferings.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
            ),
        )
    }

    private fun scrollToTopWhenLoadingFinish() {
        offeringAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                if (isItemExist()) {
                    scrollToTop()
                }
            }
        }
    }

    private fun isItemExist() = offeringAdapter.itemCount > 0

    private fun setUpOfferingsObserve() {
        viewModel.offerings.observe(viewLifecycleOwner) {
            offeringAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.searchEvent.observe(viewLifecycleOwner) {
            offeringAdapter.setSearchKeyword(it)
        }
    }

    override fun onClick(offeringId: Long) {
        OfferingDetailActivity.startActivity(activity as Context, offeringId)
    }

    private fun scrollToTop() {
        binding.rvOfferings.smoothScrollToPosition(0)
    }

    private fun navigateToOfferingWriteFragment() {
        binding.fabCreateOffering.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_offering_write_fragment)
        }
    }
}