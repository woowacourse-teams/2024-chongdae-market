package com.zzang.chongdae.presentation.view.offeringdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.ActivityOfferingDetailBinding
import com.zzang.chongdae.presentation.util.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.view.commentdetail.CommentDetailActivity

class OfferingDetailActivity : AppCompatActivity() {
    private var _binding: ActivityOfferingDetailBinding? = null
    private val binding get() = _binding!!
    private val offeringId by lazy {
        obtainOfferingId()
    }
    private val viewModel: OfferingDetailViewModel by viewModels {
        OfferingDetailViewModel.getFactory(
            offeringId = offeringId,
            offeringDetailRepository = (application as ChongdaeApp).offeringDetailRepository,
            applicationContext,
        )
    }

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by lazy {
        FirebaseAnalyticsManager(firebaseAnalytics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setUpMoveCommentDetailEventObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initBinding() {
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_offering_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun obtainOfferingId() =
        intent.getLongExtra(
            EXTRA_OFFERING_ID_KEY,
            EXTRA_DEFAULT_VALUE,
        )

    private fun setUpMoveCommentDetailEventObserve() {
        viewModel.commentDetailEvent.observe(this) { offeringTitle ->

            firebaseAnalyticsManager.logSelectContentEvent(
                id = "Offering_Item_ID: $offeringId",
                name = "participate_offering_event",
                contentType = "button",
            )
            CommentDetailActivity.startActivity(this, offeringId, offeringTitle)
        }
    }

    companion object {
        private const val EXTRA_DEFAULT_VALUE = -1L
        private const val EXTRA_OFFERING_ID_KEY = "offering_id_key"

        fun startActivity(
            context: Context,
            offeringId: Long,
        ) = Intent(context, OfferingDetailActivity::class.java).run {
            putExtra(EXTRA_OFFERING_ID_KEY, offeringId)
            context.startActivity(this)
        }
    }
}
