package com.mabrouk.hadith_feature.presentaion.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.hadith_feature.R
import com.mabrouk.hadith_feature.databinding.HadithBookItemViewBinding
import com.mabrouk.hadith_feature.domain.models.HadithBookNumber

class HadithBookAdapter(val onBookClick: (item: HadithBookNumber) -> Unit) :
    RecyclerView.Adapter<HadithBookAdapter.Holder>() {
    var data: ArrayList<HadithBookNumber> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(private val viewBinding: HadithBookItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: HadithBookNumber) {
            viewBinding.category = item
            viewBinding.root.setOnClickListener {
                onBookClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.hadith_book_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}