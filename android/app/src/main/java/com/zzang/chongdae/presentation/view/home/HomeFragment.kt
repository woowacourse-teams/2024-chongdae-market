package com.zzang.chongdae.presentation.view.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzang.chongdae.R
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.impl.OfferingsDataSourceImpl
import com.zzang.chongdae.data.repository.remote.OfferingsRepositoryImpl
import com.zzang.chongdae.databinding.FragmentHomeBinding
import com.zzang.chongdae.presentation.view.detail.OfferingDetailActivity
import com.zzang.chongdae.presentation.view.home.adapter.OfferingAdapter

class HomeFragment : Fragment(), OnArticleClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var offeringAdapter: OfferingAdapter
    private val viewModel: OfferingViewModel by viewModels {
        OfferingViewModeFactory(
            OfferingsRepositoryImpl(
                OfferingsDataSourceImpl(NetworkManager.offeringsService()),
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        offeringAdapter = OfferingAdapter(this)
        binding.rvOfferings.adapter = offeringAdapter
        binding.rvOfferings.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
            ),
        )
        viewModel.offerings.observe(viewLifecycleOwner) {
            offeringAdapter.submitList(it)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateArticles()
    }

    override fun onClick(offeringId: Long) {
        OfferingDetailActivity.startActivity(activity as Context, offeringId)
    }
}
