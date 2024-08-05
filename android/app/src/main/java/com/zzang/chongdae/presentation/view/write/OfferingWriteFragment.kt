package com.zzang.chongdae.presentation.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zzang.chongdae.R
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.impl.OfferingsDataSourceImpl
import com.zzang.chongdae.data.repository.remote.OfferingsRepositoryImpl
import com.zzang.chongdae.databinding.FragmentOfferingWriteBinding
import com.zzang.chongdae.presentation.view.MainActivity

class OfferingWriteFragment : Fragment() {
    private var _binding: FragmentOfferingWriteBinding? = null
    private val binding get() = _binding!!
    private var toast: Toast? = null

    private val viewModel: OfferingWriteViewModel by viewModels {
        OfferingWriteViewModel.getFactory(
            OfferingsRepositoryImpl(
                OfferingsDataSourceImpl(
                    NetworkManager.offeringsService(),
                ),
            ),
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
        (activity as MainActivity).hideBottomNavigation()
        observeInvalidInputEvent()
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentOfferingWriteBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun observeInvalidInputEvent() {
        viewModel.invalidTotalCountEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_invalid_total_count)
        }
        viewModel.invalidTotalPriceEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_invalid_total_price)
        }
        viewModel.invalidEachPriceEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_invalid_each_price)
        }
    }

    private fun showToast(
        @StringRes message: Int,
    ) {
        toast?.cancel()
        toast =
            Toast.makeText(
                requireActivity(),
                message,
                Toast.LENGTH_SHORT,
            )
        toast?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
