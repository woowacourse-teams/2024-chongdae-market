package com.zzang.chongdae.presentation.view.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.impl.CommentRoomDataSourceImpl
import com.zzang.chongdae.data.repository.remote.CommentRoomRepositoryImpl
import com.zzang.chongdae.databinding.FragmentCommentRoomBinding
import com.zzang.chongdae.presentation.view.comment.adapter.CommentRoomAdapter

class CommentRoomFragment : Fragment() {
    private var _binding: FragmentCommentRoomBinding? = null
    private val binding get() = _binding!!

    private val commentRoomAdapter: CommentRoomAdapter by lazy{
        CommentRoomAdapter(viewModel)
    }

    val viewModel by viewModels<CommentRoomViewModel> {
        CommentRoomViewModel.factory(
            CommentRoomRepositoryImpl(
                CommentRoomDataSourceImpl(NetworkManager.commentRoomService()),
            ),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initBinding(inflater, container)
        linkAdapter()
        updateCommentRooms()

        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentCommentRoomBinding.inflate(inflater, container, false)
        binding.fragmentCommentRoom = this
        binding.vm = viewModel
    }

    private fun linkAdapter() {
        binding.rvCommentRoom.adapter = commentRoomAdapter
        viewModel.commentRooms.observe(viewLifecycleOwner) {
            commentRoomAdapter.submitList(it)
        }
        commentRoomAdapter.submitList(viewModel.commentRooms.value)
    }

    private fun updateCommentRooms() {
        viewModel.updateCommentRooms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
