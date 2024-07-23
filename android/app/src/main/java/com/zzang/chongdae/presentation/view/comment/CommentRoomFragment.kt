package com.zzang.chongdae.presentation.view.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.FragmentCommentRoomBinding
import com.zzang.chongdae.presentation.view.comment.adapter.CommentRoomAdapter

class CommentRoomFragment : Fragment() {
    private var _binding: FragmentCommentRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var _commentRoomAdapter: CommentRoomAdapter
    val commentRoomAdapter get() = _commentRoomAdapter

    private val viewModel = CommentRoomViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCommentRoomBinding.inflate(inflater, container, false)
        binding.fragmentCommentRoom = this
        _commentRoomAdapter = CommentRoomAdapter(viewModel)
        binding.rvCommentRoom.adapter = _commentRoomAdapter

        return inflater.inflate(R.layout.fragment_comment_room, container, false)
    }
}
