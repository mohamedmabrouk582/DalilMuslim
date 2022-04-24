package com.mabrouk.hadith_feature.presentaion.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.hadith_feature.R
import com.mabrouk.hadith_feature.databinding.HadithCategoryItemViewBinding
import com.mabrouk.hadith_feature.domain.models.HadithCategory

class HadithCategoryAdapter(val onCategoryClick: (item: HadithCategory) -> Unit) :
    RecyclerView.Adapter<HadithCategoryAdapter.Holder>() {
    var data: ArrayList<HadithCategory> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(private val viewBinding: HadithCategoryItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: HadithCategory) {
            viewBinding.category = item
            viewBinding.root.setOnClickListener {
                onCategoryClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.hadith_category_item_view,
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