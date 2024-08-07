package com.zzang.chongdae.presentation.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zzang.chongdae.databinding.ItemOfferingBinding
import com.zzang.chongdae.presentation.view.home.OfferingUiModel
import com.zzang.chongdae.presentation.view.home.OnOfferingClickListener

class OfferingAdapter(
    private val onOfferingClickListener: OnOfferingClickListener,
) : PagingDataAdapter<OfferingUiModel, OfferingViewHolder>(productComparator) {
    private var searchKeyword: String? = null

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
        if (searchKeyword != null) {
            getItem(position)?.let {
                val isSearchedItem = (searchKeyword as String) in it.offering.title
                holder.bind(it.copy(isSearched = isSearchedItem), onOfferingClickListener, searchKeyword)
            }
        }
        getItem(position)?.let { holder.bind(it, onOfferingClickListener, searchKeyword) }
    }

    fun setSearchKeyword(keyword: String?) {
        searchKeyword = keyword
    }

    companion object {
        private val productComparator =
            object : DiffUtil.ItemCallback<OfferingUiModel>() {
                override fun areItemsTheSame(
                    oldItem: OfferingUiModel,
                    newItem: OfferingUiModel,
                ): Boolean {
                    return oldItem.isSearched == newItem.isSearched
                }

                override fun areContentsTheSame(
                    oldItem: OfferingUiModel,
                    newItem: OfferingUiModel,
                ): Boolean {
                    return oldItem.offering == newItem.offering
                }
            }
    }
}
