package com.zzang.chongdae.presentation.view.commentdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.ActivityCommentDetailBinding

class CommentDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCommentDetailBinding? = null
    private val binding get() = _binding!!

    private val offeringId by lazy {
        intent.getLongExtra(
            EXTRA_OFFERING_ID_KEY,
            EXTRA_DEFAULT_VALUE,
        )
    }

    private val offeringTitle by lazy {
        intent.getStringExtra(EXTRA_OFFERING_TITLE_KEY) ?: ""
    }

    private val viewModel: CommentDetailViewModel by viewModels { CommentDetailViewModel.getFactory(offeringId, offeringTitle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initBinding() {
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    companion object {
        private const val EXTRA_DEFAULT_VALUE = -1L
        private const val EXTRA_OFFERING_ID_KEY = "offering_id_key"
        private const val EXTRA_OFFERING_TITLE_KEY = "offering_title_key"

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
