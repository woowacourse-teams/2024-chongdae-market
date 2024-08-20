package com.zzang.chongdae.presentation.view.comment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.databinding.FragmentCommentRoomsBinding
import com.zzang.chongdae.presentation.util.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.view.comment.adapter.CommentRoomsAdapter
import com.zzang.chongdae.presentation.view.comment.adapter.OnCommentRoomClickListener
import com.zzang.chongdae.presentation.view.commentdetail.CommentDetailActivity

class CommentRoomsFragment : Fragment(), OnCommentRoomClickListener {
    private var _binding: FragmentCommentRoomsBinding? = null
    private val binding get() = _binding!!

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by lazy {
        FirebaseAnalyticsManager(firebaseAnalytics)
    }

    private val commentRoomsAdapter: CommentRoomsAdapter by lazy {
        CommentRoomsAdapter(this)
    }

    private val viewModel by viewModels<CommentRoomsViewModel> {
        CommentRoomsViewModel.getFactory(
            authRepository = (requireActivity().application as ChongdaeApp).authRepository,
            commentRoomsRepository = (requireActivity().application as ChongdaeApp).commentRoomsRepository,
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

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentCommentRoomsBinding.inflate(inflater, container, false)
        binding.fragmentCommentRooms = this
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun linkAdapter() {
        binding.rvCommentRoom.adapter = commentRoomsAdapter
        viewModel.commentRooms.observe(viewLifecycleOwner) {
            commentRoomsAdapter.submitList(it)
        }
        commentRoomsAdapter.submitList(viewModel.commentRooms.value)
    }

    private fun updateCommentRooms() {
        viewModel.updateCommentRooms()
    }

    override fun onResume() {
        super.onResume()
        updateCommentRooms()
        firebaseAnalyticsManager.logScreenView(
            screenName = "CommentRoomsFragment",
            screenClass = this::class.java.simpleName,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(
        id: Long,
        title: String,
    ) {
        CommentDetailActivity.startActivity(activity as Context, id, title)
    }
}
