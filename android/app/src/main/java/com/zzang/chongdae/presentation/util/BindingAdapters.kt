package com.zzang.chongdae.presentation.util

import android.animation.ValueAnimator
import android.content.Context
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.OfferingCondition
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.regex.Pattern

@BindingAdapter("url")
fun TextView.setHyperlink(url: String?) {
    url?.let {
        val mTransform = Linkify.TransformFilter { _, _ -> "" }
        val pattern = Pattern.compile(this.text.toString())
        Linkify.addLinks(this, pattern, it, null, mTransform)
    }
}

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

@BindingAdapter("offeringsProductImageUrl")
fun ImageView.setOfferingsProductImageResource(imageUrl: String?) {
    imageUrl.let {
        Glide.with(context)
            .load(it)
            .error(R.drawable.img_main_product_default)
            .fallback(R.drawable.img_main_product_default)
            .into(this)
    }
}

@BindingAdapter("offeringCondition")
fun TextView.bindConditionText(offeringCondition: OfferingCondition?) {
    offeringCondition?.toStyle()?.let {
        this.setTextAppearance(it)
    }

    offeringCondition?.let {
        this.text = it.toComment(context)
        this.setTextAppearance(it.toStyle())
    }
}

private fun OfferingCondition.toComment(context: Context) =
    when (this) {
        OfferingCondition.FULL -> context.getString(R.string.main_offering_full) // 인원 만석
        OfferingCondition.TIME_OUT -> context.getString(R.string.main_offering_closed) // 공구마감
        OfferingCondition.CONFIRMED -> context.getString(R.string.main_offering_closed) // 공구마감
        OfferingCondition.AVAILABLE -> context.getString(R.string.main_offering_continue) // 모집중
    }

private fun OfferingCondition.toStyle() =
    when (this) {
        OfferingCondition.FULL -> R.style.Theme_AppCompat_TextView_Offering_Full // 인원 만석
        OfferingCondition.TIME_OUT -> R.style.Theme_AppCompat_TextView_Offering_Closed // 공구마감
        OfferingCondition.CONFIRMED -> R.style.Theme_AppCompat_TextView_Offering_Closed // 공구마감
        OfferingCondition.AVAILABLE -> R.style.Theme_AppCompat_TextView_Offering_Continue // 모집중
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

@BindingAdapter("formattedTime")
fun TextView.setTime(localDateTime: LocalDateTime) {
    val aMOrPm = if (localDateTime.hour < 12) context.getString(R.string.am) else context.getString(R.string.pm)
    val hour = if (aMOrPm == context.getString(R.string.am)) localDateTime.hour else localDateTime.hour - 12
    val minute = localDateTime.format(DateTimeFormatter.ofPattern(context.getString(R.string.time)))
    this.text = "$aMOrPm $hour:$minute"
}

private fun Int.toPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics,
    ).toInt()
}

@BindingAdapter("formattedDeadline")
fun setFormattedDeadline(
    textView: TextView,
    deadline: LocalDateTime?,
) {
    deadline?.let {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE a h시 m분", Locale.KOREAN)
        textView.text = it.format(formatter)
    }
}
