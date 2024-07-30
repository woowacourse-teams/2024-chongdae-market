package com.zzang.chongdae.presentation.view.commentdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.zzang.chongdae.R
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.impl.CommentDetailDataSourceImpl
import com.zzang.chongdae.data.repository.remote.CommentDetailRepositoryImpl
import com.zzang.chongdae.databinding.ActivityCommentDetailBinding
import com.zzang.chongdae.presentation.view.commentdetail.adapter.CommentAdapter

class CommentDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCommentDetailBinding? = null
    private val binding get() = _binding!!
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter() }
    private val viewModel: CommentDetailViewModel by viewModels {
        CommentDetailViewModel.getFactory(
            offeringId,
            offeringTitle,
            CommentDetailRepositoryImpl(
                CommentDetailDataSourceImpl(
                    NetworkManager.offeringsService(),
                    NetworkManager.commentsService(),
                ),
            ),
        )
    }

    private val offeringId by lazy {
        intent.getLongExtra(
            EXTRA_OFFERING_ID_KEY,
            EXTRA_DEFAULT_VALUE,
        )
    }
    private val offeringTitle by lazy {
        intent.getStringExtra(EXTRA_OFFERING_TITLE_KEY) ?: DEFAULT_OFFERING_TITLE
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupDrawerToggle()
        initAdapter()
        setUpCommentsObserve()
    }
    
    private fun initBinding() {
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
    
    private fun setupDrawerToggle() {
        binding.ivMore.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }
    
    private fun initAdapter() {
        binding.rvComments.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@CommentDetailActivity).apply {
                stackFromEnd = true
            }
        }
    }
    
    private fun setUpCommentsObserve() {
        viewModel.comments.observe(this) { comments ->
            commentAdapter.submitList(comments) {
                binding.rvComments.doOnPreDraw {
                    binding.rvComments.scrollToPosition(comments.size - 1)
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
    companion object {
        private const val EXTRA_DEFAULT_VALUE = 1L
        private const val EXTRA_OFFERING_ID_KEY = "offering_id_key"
        private const val EXTRA_OFFERING_TITLE_KEY = "offering_title_key"
        private const val DEFAULT_OFFERING_TITLE = "공구 제목을 불러오지 못했어요."

        fun startActivity(
            context: Context,
            offeringId: Long,
            offeringTitle: String,
        ) = Intent(context, CommentDetailActivity::class.java).run {
            putExtra(EXTRA_OFFERING_ID_KEY, offeringId)
            putExtra(EXTRA_OFFERING_TITLE_KEY, offeringTitle)
            context.startActivity(this)
        }
    }
}
