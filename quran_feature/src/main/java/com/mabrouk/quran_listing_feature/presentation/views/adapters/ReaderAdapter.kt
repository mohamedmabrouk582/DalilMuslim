package com.mabrouk.quran_listing_feature.presentation.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.ReaderItemViewBinding
import com.mabrouk.quran_listing_feature.domain.models.QuranReader


/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 03/09/2021 created by Just clean
 */
class ReaderAdapter(val onItemClick: (item: QuranReader) -> Unit) :
    RecyclerView.Adapter<ReaderAdapter.Holder>() {
    var lastPos = -1
    var items: ArrayList<QuranReader> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(private val viewBinding: ReaderItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: QuranReader) {
            viewBinding.reader = item
            viewBinding.root.setOnClickListener {
                if (lastPos != absoluteAdapterPosition){
                    if (lastPos > -1){
                        items[lastPos].isSelected = false
                        notifyItemChanged(lastPos)
                    }
                    items[absoluteAdapterPosition].isSelected = true
                    notifyItemChanged(absoluteAdapterPosition)
                    lastPos = absoluteAdapterPosition
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.reader_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}