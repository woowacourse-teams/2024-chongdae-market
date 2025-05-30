package com.zzang.chongdae.presentation.view.nickname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.Fragment
import com.zzang.chongdae.databinding.FragmentEditNicknameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNicknameFragment : Fragment() {
    private var _binding: FragmentEditNicknameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            MaterialTheme {
                EditNicknameScreen(
                    onBack = {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    },
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
