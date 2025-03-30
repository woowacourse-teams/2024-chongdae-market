package com.zzang.chongdae.presentation.view.offeringdetail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zzang.chongdae.databinding.DialogOfferingDetailBottomSheetBinding
import com.zzang.chongdae.presentation.view.main.MainActivity
import com.zzang.chongdae.presentation.view.offeringdetail.OfferingDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfferingDetailBottomSheetDialog(
    private val offeringId: Long,
) : BottomSheetDialogFragment() {
    private var _binding: DialogOfferingDetailBottomSheetBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var offeringDetailAssistedFactory: OfferingDetailViewModel.OfferingDetailAssistedFactory

    private val viewModel: OfferingDetailViewModel by activityViewModels {
        OfferingDetailViewModel.getFactory(
            assistedFactory = offeringDetailAssistedFactory,
            offeringId = offeringId,
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = DialogOfferingDetailBottomSheetBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
}
