package com.zzang.chongdae.presentation.view.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zzang.chongdae.databinding.ItemOfferingBinding
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

class OfferingAdapter(
    private val onOfferingClickListener: OnOfferingClickListener,
) : PagingDataAdapter<Offering, OfferingViewHolder>(productComparator) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OfferingViewHolder {
        val binding =
            ItemOfferingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OfferingViewHolder,
        position: Int,
    ) {
        Log.e("seogi", "${getItem(position)?.id}")
        getItem(position)?.let { holder.bind(it, onOfferingClickListener) }
    }

    companion object {
        private val productComparator =
            object : DiffUtil.ItemCallback<Offering>() {
                override fun areItemsTheSame(
                    oldItem: Offering,
                    newItem: Offering,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Offering,
                    newItem: Offering,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
