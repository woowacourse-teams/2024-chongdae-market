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

    private lateinit var _commentRoomAdapter: CommentRoomAdapter
    val commentRoomAdapter get() = _commentRoomAdapter

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
        _binding = FragmentCommentRoomBinding.inflate(inflater, container, false)
        binding.fragmentCommentRoom = this
        _commentRoomAdapter = CommentRoomAdapter(viewModel)
        binding.rvCommentRoom.adapter = _commentRoomAdapter
        viewModel.commentRooms.observe(viewLifecycleOwner) {
            commentRoomAdapter.submitList(it)
        }
        commentRoomAdapter.submitList(viewModel.commentRooms.value)
        // 테스트를 위해 주석처리함!
//        viewModel.updateCommentRooms()

        return binding.root
    }
}
