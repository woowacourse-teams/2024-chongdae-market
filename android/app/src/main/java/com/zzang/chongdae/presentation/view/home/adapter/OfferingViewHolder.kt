package com.zzang.chongdae.presentation.view.home.adapter

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemOfferingBinding
import com.zzang.chongdae.presentation.view.home.OfferingUiModel
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

class OfferingViewHolder(
    private val binding: ItemOfferingBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        offeringUiModel: OfferingUiModel,
        onOfferingClickListener: OnOfferingClickListener,
        searchKeyword: String?,
    ) {
        binding.offering = offeringUiModel.offering
        binding.onOfferingClickListener = onOfferingClickListener
        binding.searchKeyword = searchKeyword
        setCancellationLineToEachPrice()
    }

    private fun setCancellationLineToEachPrice() {
        binding.tvOriginPrice.apply {
            this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}
