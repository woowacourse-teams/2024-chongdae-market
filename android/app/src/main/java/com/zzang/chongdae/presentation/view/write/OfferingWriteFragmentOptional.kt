package com.zzang.chongdae.presentation.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.databinding.FragmentOfferingWriteOptionalBinding

class OfferingWriteFragmentOptional : Fragment() {
    private var _fragmentBinding: FragmentOfferingWriteOptionalBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    private var toast: Toast? = null

    private val viewModel: OfferingWriteViewModel by viewModels {
        OfferingWriteViewModel.getFactory(
            offeringRepository = (requireActivity().application as ChongdaeApp).offeringRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        return fragmentBinding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _fragmentBinding = FragmentOfferingWriteOptionalBinding.inflate(inflater, container, false)
        fragmentBinding.vm = viewModel
        fragmentBinding.lifecycleOwner = viewLifecycleOwner
    }
}