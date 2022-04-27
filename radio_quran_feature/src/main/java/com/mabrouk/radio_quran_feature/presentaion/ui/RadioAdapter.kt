package com.mabrouk.radio_quran_feature.presentaion.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.radio_quran_feature.R
import com.mabrouk.radio_quran_feature.databinding.RadioItemViewBinding
import com.mabrouk.radio_quran_feature.domain.models.Radio


/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 07/09/2021 created by Just clean
 */
class RadioAdapter(val onItemClick: (item: Radio) -> Unit) :
    RecyclerView.Adapter<RadioAdapter.Holder>() {
    var items: ArrayList<Radio> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(private val viewBinding: RadioItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Radio) {
            viewBinding.radio = item
            viewBinding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.radio_item_view,
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