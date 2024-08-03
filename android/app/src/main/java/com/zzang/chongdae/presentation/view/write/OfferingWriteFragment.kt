package com.zzang.chongdae.presentation.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.impl.OfferingWriteDataSourceImpl
import com.zzang.chongdae.data.repository.remote.OfferingWriteRepositoryImpl
import com.zzang.chongdae.databinding.FragmentOfferingWriteBinding
import com.zzang.chongdae.presentation.view.MainActivity

class OfferingWriteFragment : Fragment() {
    private var _binding: FragmentOfferingWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OfferingWriteViewModel by viewModels {
        OfferingWriteViewModel.getFactory(
            OfferingWriteRepositoryImpl(
                OfferingWriteDataSourceImpl(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentOfferingWriteBinding.inflate(inflater, container, false)
        binding.vm = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
