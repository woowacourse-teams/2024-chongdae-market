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
    private var searchKeyword: String? = null
    private var updatedOfferings: List<Offering> = emptyList()

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
        getItem(position)?.let { offering ->
            val updatedOffering = updatedOfferings.firstOrNull { offering.id == it.id }
            if (updatedOffering != null) {
                holder.bind(
                    offering.copy(
                        currentCount = updatedOffering.currentCount,
                        status = updatedOffering.status
                    ), onOfferingClickListener, searchKeyword
                )
                return
            }
            holder.bind(offering, onOfferingClickListener, searchKeyword)
        }
    }

    fun setSearchKeyword(keyword: String?) {
        searchKeyword = keyword
    }

    fun addUpdatedItem(updatedOfferings: List<Offering>) {
        Log.e("seogi","${updatedOfferings}")
        this.updatedOfferings = updatedOfferings
        updatedOfferings.forEach { offering ->
            val position = findPositionByOfferingID(offering)
            if (position != -1) {
                notifyItemChanged(position)
            }
        }
    }

    private fun findPositionByOfferingID(offering: Offering) =
        snapshot().items.indexOfFirst { it.id == offering.id }

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
