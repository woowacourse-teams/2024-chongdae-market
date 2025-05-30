package com.zzang.chongdae.presentation.view.commentdetail

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
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
import com.zzang.chongdae.R
import com.zzang.chongdae.common.firebase.FirebaseAnalyticsManager
import com.zzang.chongdae.databinding.ActivityCommentDetailBinding
import com.zzang.chongdae.databinding.DialogAlertBinding
import com.zzang.chongdae.databinding.DialogUpdateStatusBinding
import com.zzang.chongdae.presentation.view.commentdetail.adapter.comment.CommentAdapter
import com.zzang.chongdae.presentation.view.commentdetail.adapter.participant.ParticipantAdapter
import com.zzang.chongdae.presentation.view.commentdetail.event.CommentDetailEvent
import com.zzang.chongdae.presentation.view.commentdetail.event.OnUpdateStatusClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentDetailActivity : AppCompatActivity(), OnUpdateStatusClickListener {
    private var _binding: ActivityCommentDetailBinding? = null
    private val binding get() = _binding!!
    private var toast: Toast? = null
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter() }
    private val participantAdapter: ParticipantAdapter by lazy { ParticipantAdapter() }
    private val dialog: Dialog by lazy { Dialog(this) }

    @Inject
    lateinit var commentDetailAssistedFactory: CommentDetailViewModel.CommentDetailAssistedFactory

    private val viewModel: CommentDetailViewModel by viewModels {
        CommentDetailViewModel.getFactory(
            assistedFactory = commentDetailAssistedFactory,
            offeringId = offeringId,
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
        initAdapter()
        setUpObserve()
    }

    private fun initBinding() {
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this
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
        observeEvent()
    }

    private fun observeComments() {
        viewModel.comments.observe(this) { comments ->
            commentAdapter.submitList(comments)
            binding.rvComments.doOnPreDraw {
                binding.rvComments.scrollToPosition(comments.size - 1)
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

    private fun observeEvent() {
        viewModel.event.observe(this) { event ->
            event.getContentIfNotHandled()?.let { handleEvent(it) }
        }
    }

    private fun handleEvent(event: CommentDetailEvent) {
        when (event) {
            is CommentDetailEvent.BackPressed -> finish()
            is CommentDetailEvent.ShowError -> showError(event.message)
            is CommentDetailEvent.ShowReport -> reportEvent(event.reportUrlId)
            is CommentDetailEvent.ShowUpdateStatusDialog -> showUpdateStatusDialog()
            is CommentDetailEvent.ShowAlert -> showExitDialog()
            is CommentDetailEvent.ExitOffering -> exitOfferingEvent()
            is CommentDetailEvent.AlertCancelled -> cancelDialog()
            is CommentDetailEvent.LogAnalytics ->
                firebaseAnalyticsManager.logSelectContentEvent(
                    id = event.eventId,
                    name = event.eventId,
                    contentType = "button",
                )
            is CommentDetailEvent.OpenDrawer -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.END)
                }
            }
        }
    }

    private fun showError(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun reportEvent(reportUrlId: Int) {
        openUrlInBrowser(getString(reportUrlId))
    }

    private fun openUrlInBrowser(url: String) {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
        startActivity(intent)
    }

    private fun exitOfferingEvent() {
        firebaseAnalyticsManager.logSelectContentEvent(
            id = "exit_offering_event",
            name = "exit_offering_event",
            contentType = "button",
        )
        finish()
        dialog.dismiss()
    }

    private fun showExitDialog() {
        val alertBinding = DialogAlertBinding.inflate(layoutInflater, null, false)
        alertBinding.tvDialogMessage.text = getString(R.string.comment_detail_exit_alert)
        alertBinding.listener = viewModel
        dialog.setContentView(alertBinding.root)
        dialog.show()
    }

    private fun cancelDialog() {
        dialog.dismiss()
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
        const val EXTRA_OFFERING_ID_KEY = "offering_id_key"

        fun startActivity(
            context: Context,
            offeringId: Long,
        ) = Intent(context, CommentDetailActivity::class.java).run {
            putExtra(EXTRA_OFFERING_ID_KEY, offeringId)
            context.startActivity(this)
        }
    }
}
