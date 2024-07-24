package com.zzang.chongdae.presentation.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.zzang.chongdae.databinding.ItemOfferingBinding
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.presentation.view.home.OnArticleClickListener

class OfferingAdapter(
    private val onArticleClickListener: OnArticleClickListener,
) : ListAdapter<Offering, OfferingViewHolder>(productComparator) {

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
        holder.bind(getItem(position), onArticleClickListener)
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
