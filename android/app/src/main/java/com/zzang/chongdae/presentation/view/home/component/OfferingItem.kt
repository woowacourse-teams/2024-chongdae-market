package com.zzang.chongdae.presentation.view.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zzang.chongdae.R
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OfferingItem(
    offering: Offering,
    onOfferingClick: OnOfferingClickListener,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onOfferingClick.onClick(offering.id) },
            ),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(vertical = 25.dp)
                .padding(start = 4.dp)
        ) {
            Box {
                GlideImage(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    model = offering.thumbnailUrl,
                    contentDescription = "thumbnail",
                    contentScale = ContentScale.Crop,
                ) {
                    it
                        .load(offering.thumbnailUrl)
                        .error(R.drawable.img_main_product_default)
                        .fallback(R.drawable.img_main_product_default)
                }
                OfferingStatusText(offering.status, density)
            }

            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Text(
                    text = offering.title,
                    fontSize = with(density) { 16.dp.toSp() },
                    fontFamily = FontFamily(
                        Font(
                            R.font.suit_bold,
                            weight = FontWeight.Bold,
                        )
                    ),
                    color = colorResource(R.color.black_900),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = offering.meetingAddressDong,
                    fontSize = with(density) { 12.dp.toSp() },
                    fontFamily = FontFamily(
                        Font(
                            R.font.suit_medium,
                            weight = FontWeight.Medium,
                        )
                    ),
                    color = colorResource(R.color.gray_500)
                )

                Text(
                    text = offering.status.toOfferingComment(offering.totalCount - offering.currentCount),
                    fontSize = with(density) { 12.dp.toSp() },
                    fontFamily = FontFamily(
                        Font(
                            R.font.suit_medium,
                            weight = FontWeight.Medium,
                        )
                    ),
                    color = colorResource(R.color.gray_700)
                )

                Text(
                    modifier = Modifier.padding(top = 35.dp),
                    text = if (offering.originPrice != null) {
                        stringResource(R.string.all_money_amount_text, offering.originPrice)
                    } else "",
                    textDecoration = TextDecoration.combine(
                        listOf(
                            TextDecoration.LineThrough,
                        )
                    ),
                    fontSize = with(density) { 12.dp.toSp() },
                    fontFamily = FontFamily(
                        Font(
                            R.font.suit_medium,
                            weight = FontWeight.Medium,
                        )
                    ),
                    color = colorResource(R.color.gray_500),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.padding(top = 50.dp),
                    text = if (offering.originPrice != null) {
                        stringResource(
                            R.string.home_discount_rate_text,
                            offering.calculateDiscountRate() ?: 0f
                        )
                    } else "",
                    fontSize = with(density) { 13.dp.toSp() },
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.main_color)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.all_money_amount_text, offering.dividedPrice),
                    fontSize = with(density) { 25.dp.toSp() },
                    fontFamily = FontFamily(
                        Font(
                            R.font.suit_bold,
                            weight = FontWeight.Bold,
                        )
                    ),
                    color = colorResource(R.color.main_color),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun OfferingCondition.toOfferingComment(
    remaining: Int,
): AnnotatedString = when (this) {
    OfferingCondition.FULL -> AnnotatedString(stringResource(R.string.home_offering_condition_full_comment))
    OfferingCondition.IMMINENT -> buildAnnotatedString {
        val text =
            stringResource(R.string.home_offering_condition_continue_comment).format(remaining)
        val startIndex = text.indexOf("$remaining")
        val endIndex = startIndex + "$remaining".length

        append(text)
        addStyle(
            style = SpanStyle(color = colorResource(R.color.main_color)),
            start = startIndex,
            end = endIndex
        )
    }

    OfferingCondition.CONFIRMED -> AnnotatedString("")
    OfferingCondition.AVAILABLE -> AnnotatedString("")
}

@Composable
private fun OfferingStatusText(offeringCondition: OfferingCondition, density: Density) {
    val text = offeringCondition.toOfferingConditionText()
    val textStyle = offeringCondition.toTextStyle(density)
    val backgroundColor = offeringCondition.toBackgroundColor()

    Text(
        text = text,
        style = textStyle,
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
private fun OfferingCondition.toTextStyle(density: Density): TextStyle {
    val fontSize = with(density) { 12.dp.toSp() }
    val fontFamily = FontFamily(Font(R.font.suit_bold, weight = FontWeight.Bold))
    val textColor = Color.White

    return TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        fontFamily = fontFamily,
        color = textColor
    )
}

@Composable
private fun OfferingCondition.toBackgroundColor(): Color {
    return when (this) {
        OfferingCondition.FULL -> colorResource(R.color.offering_full)
        OfferingCondition.IMMINENT -> colorResource(R.color.offering_imminent)
        OfferingCondition.CONFIRMED -> colorResource(R.color.offering_closed)
        OfferingCondition.AVAILABLE -> colorResource(R.color.offering_continue)
    }
}

@Composable
private fun OfferingCondition.toOfferingConditionText(): String =
    when (this) {
        OfferingCondition.FULL -> stringResource(R.string.home_offering_full)
        OfferingCondition.IMMINENT -> stringResource(R.string.home_offering_imminent)
        OfferingCondition.CONFIRMED -> stringResource(R.string.home_offering_closed)
        OfferingCondition.AVAILABLE -> stringResource(R.string.home_offering_continue)
    }


@Preview(showSystemUi = true)
@Composable
fun OfferingItemPreview(modifier: Modifier = Modifier) {
    val offering = Offering(
        id = 1,
        title = "제목",
        meetingAddressDong = "XXX동",
        thumbnailUrl = "https://cdn.pixabay.com/photo/2023/12/08/05/41/cat-8436848_1280.jpg",
        totalCount = 5,
        currentCount = 3,
        dividedPrice = 1000,
        originPrice = 10000,
        status = OfferingCondition.IMMINENT,
        isOpen = false
    )
    OfferingItem(offering, object : OnOfferingClickListener {
        override fun onClick(offeringId: Long) {

        }
    })
}
