package com.mabrouk.history_feature.peresntaion.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.history_feature.R
import com.mabrouk.history_feature.databinding.StoryItemViewBinding
import com.mabrouk.history_feature.domain.models.Story

class StoryAdapter(val onItemClick: (item: Story, pos: Int) -> Unit) :
    RecyclerView.Adapter<StoryAdapter.Holder>() {
    var data: ArrayList<Story> = ArrayList()
    var lastPosition: Int = -1
    fun addItem(item: Story) {
        if (!data.any { it.videoKey == item.videoKey }) {
            data.add(item)
            notifyItemInserted(data.size - 1)
        }
    }


    inner class Holder(val viewBinding: StoryItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Story) {
            viewBinding.story = item
            if (item.isPlaying) viewBinding.root.performClick()
            viewBinding.root.setOnClickListener {
                if (lastPosition != absoluteAdapterPosition) {
                    if (lastPosition != -1) {
                        data[lastPosition].isPlaying = false
                        notifyItemChanged(lastPosition)
                    }
                    item.isPlaying = true
                    lastPosition = absoluteAdapterPosition
                    notifyItemChanged(absoluteAdapterPosition)
                    onItemClick(item, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.story_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateItem(key: String) {
        data.find { it.videoKey == key }?.apply {
            isPlaying = true
            notifyItemChanged(data.indexOf(this))
        }
    }
}