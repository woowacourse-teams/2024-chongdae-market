package com.zzang.chongdae.presentation.util

import android.animation.ValueAnimator
import android.content.Context
import android.text.Html
import android.text.Spannable
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.OfferingCondition
import java.time.LocalDateTime
import java.time.LocalTime
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

@BindingAdapter("imageResource")
fun setImageResource(
    imageView: ImageView,
    @DrawableRes resource: Int?,
) {
    resource?.let { imageView.setImageResource(it) }
}

@BindingAdapter("offeringConditionForComment", "remaining")
fun TextView.bindConditionComment(
    offeringCondition: OfferingCondition?,
    remaining: Int,
) {
    offeringCondition?.let {
        this.text = it.toOfferingComment(context, remaining)
    }
}

private fun OfferingCondition.toOfferingComment(
    context: Context,
    remaining: Int,
) = when (this) {
    OfferingCondition.FULL -> context.getString(R.string.main_offering_condition_full_comment)
    OfferingCondition.TIME_OUT -> context.getString(R.string.main_offering_condition_closed_comment)
    OfferingCondition.CONFIRMED -> context.getString(R.string.main_offering_condition_closed_comment)
    OfferingCondition.AVAILABLE ->
        Html.fromHtml(
            context.getString(R.string.main_offering_condition_continue_comment)
                .format(remaining),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
}

@BindingAdapter("conditionText:offeringCondition") // 추후 condition추가(마감임박)에 따른 API변경있으면 수정 예정
fun TextView.bindConditionText(offeringCondition: OfferingCondition?) {
    offeringCondition?.toStyle()?.let {
        this.setTextAppearance(it)
    }

    offeringCondition?.let {
        this.text = it.toOfferingConditionText(context)
        this.setTextAppearance(it.toStyle())
        setBackGroundTintByCondition(it)
    }
}

private fun TextView.setBackGroundTintByCondition(offeringCondition: OfferingCondition) {
    when (offeringCondition) {
        OfferingCondition.FULL -> this.setColor(R.color.offering_full)
        OfferingCondition.TIME_OUT -> this.setColor(R.color.offering_closed)
        OfferingCondition.CONFIRMED -> this.setColor(R.color.offering_closed)
        OfferingCondition.AVAILABLE -> this.setColor(R.color.offering_continue)
    }
}

private fun TextView.setColor(colorId: Int) {
    this.background.setTint(this.context.resources.getColor(colorId, null))
}

private fun OfferingCondition.toOfferingConditionText(context: Context) =
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
    this.text = condition?.toOfferingConditionText(this.context, currentCount, totalCount)
}

private fun OfferingCondition.toOfferingConditionText(
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

@BindingAdapter("formattedAmPmTime")
fun TextView.setTime(localDateTime: LocalDateTime?) {
    this.text = localDateTime?.format(
        DateTimeFormatter.ofPattern(
            context.getString(R.string.amPmTime),
            Locale.KOREAN,
        ),
    ) ?: ""
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

@BindingAdapter("formattedCommentTime")
fun TextView.setTime(localTime: LocalTime) {
    this.text =
        localTime.format(
            DateTimeFormatter.ofPattern(
                context.getString(R.string.amPmTime),
                Locale.KOREAN,
            ),
        )
}

@BindingAdapter("setImageProposer")
fun ImageView.setImageProposer(proposer: Boolean) {
    val imageRes = if (proposer) R.drawable.ic_proposer else R.drawable.ic_not_proposer
    setImageResource(imageRes)
}

@BindingAdapter("setSubmitButtonEnabled")
fun Button.setSubmitButtonEnabled(buttonEnabled: Boolean) {
    val imageRes = if (buttonEnabled) R.drawable.btn_rounded else R.drawable.btn_rounded_disabled
    background = context.getDrawable(imageRes)
}

@BindingAdapter("setSubmitButtonText")
fun Button.setSubmitButtonText(buttonEnabled: Boolean) {
    val buttonText = if (buttonEnabled) R.string.write_start_offering else R.string.write_start_offering_button_disabled
    text = context.getText(buttonText)
}

@BindingAdapter("setSplitPriceVisibility")
fun TextView.setSplitPriceVisibility(splitPrice: Int) {
    val textVisibility = if (splitPrice > 0) View.VISIBLE else View.INVISIBLE
    visibility = textVisibility
}
