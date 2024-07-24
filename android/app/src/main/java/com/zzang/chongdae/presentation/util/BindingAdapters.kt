package com.zzang.chongdae.presentation.util

import android.animation.ValueAnimator
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.model.OfferingCondition
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("detailProductImageUrl")
fun ImageView.setImageResource(imageUrl: String?) {
    imageUrl.let {
        Glide.with(context)
            .load(it)
            .error(R.drawable.img_detail_product_default)
            .fallback(R.drawable.img_detail_product_default)
            .into(this)
    }
}

@BindingAdapter("isVisible")
fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("formattedDate")
fun TextView.bindFormattedDate(datetime: LocalDateTime?) {
    this.text =
        datetime?.format(DateTimeFormatter.ofPattern(context.getString(R.string.due_datetime)))
}

@BindingAdapter("currentCount", "totalCount", "isParticipated", "condition")
fun TextView.bindStatusComment(
    currentCount: Int,
    totalCount: Int,
    isParticipated: Boolean,
    condition: OfferingCondition?,
) {
    if (isParticipated) {
        this.text = context.getString(R.string.participant_already)
        return
    }
    this.text = condition?.toComment(this.context, currentCount, totalCount)
}

@BindingAdapter("app:showEmptyState")
fun showEmptyState(
    view: View,
    data: List<CommentRoom>?,
) {
    view.visibility = if (data?.isEmpty() == true) View.VISIBLE else View.GONE
}

private fun OfferingCondition.toComment(
    context: Context,
    currentCount: Int,
    totalCount: Int,
) = when (this) {
    OfferingCondition.FULL -> context.getString(R.string.participant_full)
    OfferingCondition.TIME_OUT -> context.getString(R.string.participant_end)
    OfferingCondition.CONFIRMED -> context.getString(R.string.participant_end)
    OfferingCondition.AVAILABLE ->
        context.getString(
            R.string.participant_count,
            currentCount,
            totalCount,
        )
}

@BindingAdapter("layout_heightWithAnimation")
fun setLayoutHeightWithAnimation(
    view: View,
    heightDp: Int,
) {
    val params: ViewGroup.LayoutParams = view.layoutParams
    val startHeight = params.height

    val heightPx = heightDp.toPx(view.context)

    val animator =
        ValueAnimator.ofInt(startHeight, heightPx).apply {
            duration = 300
            addUpdateListener { animation ->
                params.height = animation.animatedValue as Int
                view.layoutParams = params
            }
        }
    animator.start()
}

private fun Int.toPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics,
    ).toInt()
}
