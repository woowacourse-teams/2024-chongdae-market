package com.zzang.chongdae.presentation.util

import android.animation.ValueAnimator
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.model.analytics.UserType
import com.zzang.chongdae.presentation.view.commentdetail.model.usertype.UserTypeUiModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@BindingAdapter("detailProductImageUrl")
fun ImageView.setImageResource(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(context)
            .load(it)
            .error(R.drawable.img_detail_product_default)
            .fallback(R.drawable.img_detail_product_default)
            .into(this)
    }
}

@BindingAdapter("importProductImageUrl")
fun ImageView.importProductImageUrl(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.btn_upload_photo)
        .error(R.drawable.btn_upload_photo)
        .into(this)
}

@BindingAdapter("offeringsStatusImageUrl")
fun ImageView.setOfferingsStatusImageUrl(imageUrl: String?) {
    imageUrl.let {
        Glide.with(context)
            .load(it)
            .error(R.drawable.ic_comment_detail_recruiting)
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

@BindingAdapter("isVisible")
fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("isVisibleOrInvisible")
fun View.setIsVisibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("formattedDate")
fun TextView.bindFormattedDate(datetime: LocalDateTime?) {
    this.text =
        datetime?.format(DateTimeFormatter.ofPattern(context.getString(R.string.all_due_datetime)))
}

@BindingAdapter("formattedOnlyDate")
fun TextView.bindFormattedOnlyDate(datetime: LocalDate?) {
    this.text =
        datetime?.format(DateTimeFormatter.ofPattern(context.getString(R.string.all_due_datetime)))
}

@BindingAdapter("currentCount", "totalCount", "condition")
fun TextView.bindStatusComment(
    currentCount: Int,
    totalCount: Int,
    condition: OfferingCondition?,
) {
    this.text = condition?.toOfferingConditionText(this.context, currentCount, totalCount)
}

private fun OfferingCondition.toOfferingConditionText(
    context: Context,
    currentCount: Int,
    totalCount: Int,
) = when (this) {
    OfferingCondition.FULL -> context.getString(R.string.offering_detail_participant_full)
    OfferingCondition.IMMINENT ->
        context.getString(
            R.string.offering_detail_participant_count,
            currentCount,
            totalCount,
        )

    OfferingCondition.CONFIRMED -> context.getString(R.string.offering_detail_participant_end)
    OfferingCondition.AVAILABLE ->
        context.getString(
            R.string.offering_detail_participant_count,
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

@BindingAdapter("splitPriceValidity", "splitPrice")
fun TextView.setSplitPriceText(
    isSplitPriceValid: Boolean?,
    splitPrice: Int?,
) {
    val text = setSplitPrice(isSplitPriceValid, splitPrice)
    this.text = text
}

private fun TextView.setSplitPrice(
    isSplitPriceValid: Boolean?,
    splitPrice: Int?,
): String {
    if (isSplitPriceValid == true) {
        return context.getString(R.string.all_percentage_comma).format(splitPrice)
    }
    return context.getString(R.string.all_minus)
}

@BindingAdapter("discountRateValidity", "discountRate")
fun TextView.setDiscountRateValidity(
    discountRateValidity: Boolean?,
    discountRate: Float?,
) {
    val text = setDiscountRate(discountRateValidity, discountRate)
    this.text = text
}

private fun TextView.setDiscountRate(
    discountRateValidity: Boolean?,
    discountRate: Float?,
): String {
    if (discountRateValidity == true) {
        return context.getString(R.string.write_discount_rate_value).format(discountRate)
    }
    return context.getString(R.string.all_minus)
}

@BindingAdapter("originPrice")
fun EditText.setOriginPriceHint(originPrice: Int) {
    this.hint = context.getString(R.string.write_current_split_price).format(originPrice)
}

@BindingAdapter(value = ["debouncedOnClick", "debounceTime"], requireAll = false)
fun View.setDebouncedOnClick(
    clickListener: View.OnClickListener?,
    debounceTime: Long?,
) {
    val safeDebounceTime = debounceTime ?: DEFAULT_DEBOUNCE_TIME
    setDebouncedOnClickListener(safeDebounceTime) {
        clickListener?.onClick(this)
    }
}

@BindingAdapter("userTypeIcon")
fun ImageView.setUserTypeIcon(userType: UserTypeUiModel?) {
    val iconRes =
        when (userType) {
            UserTypeUiModel.A -> R.drawable.btn_more_a
            UserTypeUiModel.B -> R.drawable.btn_more_b
            else -> R.drawable.btn_more
        }
    setImageResource(iconRes)
}

@BindingAdapter("userTypeHint")
fun EditText.setUserTypeHint(userType: UserTypeUiModel?) {
    val hintRes =
        when (userType) {
            UserTypeUiModel.A -> R.string.comment_detail_message_hint_a
            UserTypeUiModel.B -> R.string.comment_detail_message_hint_b
            else -> R.string.comment_detail_message_hint
        }
    setHint(hintRes)
}

@BindingAdapter("userTypeProductLinkColor")
fun TextView.setUserTypedProductLinkColor(userType: UserType?) {
    userType?.let {
        val colorRes = if (it.typeName == "A") {
            R.color.gray_900
        } else {
            R.color.main_color
        }
        val colorInt = context.getColor(colorRes)
        setTextColor(colorInt)
    }
}
