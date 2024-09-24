package com.zzang.chongdae.presentation.view.commentdetail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.ActivityCommentDetailBinding
import com.zzang.chongdae.databinding.DialogUpdateStatusBinding
import com.zzang.chongdae.common.FirebaseAnalyticsManager
import com.zzang.chongdae.presentation.view.commentdetail.adapter.comment.CommentAdapter
import com.zzang.chongdae.presentation.view.commentdetail.adapter.participant.ParticipantAdapter

class CommentDetailActivity : AppCompatActivity(), OnUpdateStatusClickListener {
    private var _binding: ActivityCommentDetailBinding? = null
    private val binding get() = _binding!!
    private var toast: Toast? = null
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter() }
    private val participantAdapter: ParticipantAdapter by lazy { ParticipantAdapter() }
    private val dialog: Dialog by lazy { Dialog(this) }

    private val viewModel: CommentDetailViewModel by viewModels {
        CommentDetailViewModel.getFactory(
            offeringId = offeringId,
            authRepository = (application as ChongdaeApp).authRepository,
            offeringRepository = (application as ChongdaeApp).offeringRepository,
            participantRepository = (application as ChongdaeApp).participantRepository,
            commentDetailRepository = (application as ChongdaeApp).commentDetailRepository,
        )
    }

    private val offeringId by lazy {
        intent.getLongExtra(
            EXTRA_OFFERING_ID_KEY,
            EXTRA_DEFAULT_VALUE,
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
        setupDrawerToggle()
        initAdapter()
        setUpObserve()
    }

    private fun initBinding() {
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupDrawerToggle() {
        binding.ivMoreOptions.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
                return@setOnClickListener
            }
            binding.drawerLayout.openDrawer(GravityCompat.END)
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "more_comment_detail_options_event",
                name = "more_comment_detail_options_event",
                contentType = "button",
            )
        }
    }

    private fun initAdapter() {
        binding.rvComments.apply {
            adapter = commentAdapter
            layoutManager =
                LinearLayoutManager(this@CommentDetailActivity).apply {
                    stackFromEnd = true
                }
        }
        binding.rvOfferingMembers.adapter = participantAdapter
    }

    private fun setUpObserve() {
        observeComments()
        observeParticipants()
        observeUpdateOfferingEvent()
        observeReportEvent()
        observeExitOfferingEvent()
        observeBackEvent()
        observeErrorEvent()
    }

    private fun observeComments() {
        viewModel.comments.observe(this) { comments ->
            commentAdapter.submitList(comments) {
                binding.rvComments.doOnPreDraw {
                    binding.rvComments.scrollToPosition(comments.size - 1)
                }
            }
        }
    }

    private fun observeParticipants() {
        viewModel.participants.observe(this) { participants ->
            participants?.let {
                participantAdapter.submitList(it.participants)
            }
        }
    }

    private fun observeReportEvent() {
        viewModel.reportEvent.observe(this) { reportUrlId ->
            openUrlInBrowser(getString(reportUrlId))
        }
    }

    private fun openUrlInBrowser(url: String) {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
        startActivity(intent)
    }

    private fun observeUpdateOfferingEvent() {
        viewModel.showStatusDialogEvent.observe(this) {
            showUpdateStatusDialog()
        }
    }

    private fun observeExitOfferingEvent() {
        viewModel.onExitOfferingEvent.observe(this) {
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "exit_offering_event",
                name = "exit_offering_event",
                contentType = "button",
            )
            finish()
        }
    }

    private fun observeBackEvent() {
        viewModel.onBackPressedEvent.observe(this) {
            finish()
        }
    }

    private fun observeErrorEvent() {
        viewModel.errorEvent.observe(this) {
            toast?.cancel()
            toast =
                Toast.makeText(
                    this,
                    it,
                    Toast.LENGTH_SHORT,
                )
            toast?.show()
        }
    }

    private fun showUpdateStatusDialog() {
        val dialogBinding =
            DataBindingUtil.inflate<DialogUpdateStatusBinding>(
                layoutInflater,
                R.layout.dialog_update_status,
                null,
                false,
            )

        dialogBinding.vm = viewModel
        dialogBinding.listener = this

        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    override fun onSubmitClick() {
        viewModel.updateOfferingStatus()
        firebaseAnalyticsManager.logSelectContentEvent(
            id = "update_offering_status_event",
            name = "update_offering_status_event",
            contentType = "button",
        )
        dialog.dismiss()
    }

    override fun onCancelClick() {
        firebaseAnalyticsManager.logSelectContentEvent(
            id = "cancel_update_offering_status_event",
            name = "cancel_update_offering_status_event",
            contentType = "button",
        )
        dialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (view is EditText || view.id == R.id.iv_send_comment)) {
            val screenCoords = IntArray(2)
            view.getLocationOnScreen(screenCoords)
            val x = motionEvent.rawX + view.left - screenCoords[0]
            val y = motionEvent.rawY + view.top - screenCoords[1]

            if (motionEvent.action == MotionEvent.ACTION_UP &&
                (x < view.left || x >= view.right || y < view.top || y > view.bottom)
            ) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(motionEvent)
    }

    companion object {
        private const val EXTRA_DEFAULT_VALUE = 1L
        private const val EXTRA_OFFERING_ID_KEY = "offering_id_key"

        fun startActivity(
            context: Context,
            offeringId: Long,
        ) = Intent(context, CommentDetailActivity::class.java).run {
            putExtra(EXTRA_OFFERING_ID_KEY, offeringId)
            context.startActivity(this)
        }
    }
}
