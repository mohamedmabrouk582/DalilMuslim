package com.mabrouk.hadith_feature.presentaion.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.hadith_feature.R
import com.mabrouk.hadith_feature.databinding.HadithItemViewBinding
import com.mabrouk.hadith_feature.domain.models.HadithModel

class HadithAdapter() : RecyclerView.Adapter<HadithAdapter.Holder>() {
    val data:ArrayList<HadithModel> = ArrayList()

    fun setValues(data:ArrayList<HadithModel>){
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class Holder(private val viewBinding : HadithItemViewBinding) : RecyclerView.ViewHolder(viewBinding.root){
        fun bind(item:HadithModel){
            viewBinding.hadith=item
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.hadith_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}