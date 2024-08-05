package com.zzang.chongdae.presentation.view.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.zzang.chongdae.databinding.ItemOfferingBinding
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

class OfferingViewHolder(
    private val binding: ItemOfferingBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        offering: Offering,
        onOfferingClickListener: OnOfferingClickListener,
    ) {
        binding.offering = offering
        binding.onOfferingClickListener = onOfferingClickListener
    }
}
