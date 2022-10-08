package com.mabrouk.quran_listing_feature.presentation.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.JuzItemViewBinding
import com.mabrouk.quran_listing_feature.databinding.SuraItemViewBinding
import com.mabrouk.quran_listing_feature.domain.models.JuzSurah

class QuranAdapter(
    val onJuzDownload: (item: JuzSurah, postion: Int) -> Unit,
    val onSurahClick: (item: JuzSurah) -> Unit ,
    val onSurahDownload : (item : JuzSurah, position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: ArrayList<JuzSurah> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> JuzHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.juz_item_view,
                    parent,
                    false
                )
            )
            1 -> SuraHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.sura_item_view,
                    parent,
                    false
                )
            )
            else -> throw Exception("This type not found here")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is JuzHolder -> holder.bind(data[position])
            is SuraHolder -> holder.bind(data[position])
        }
    }

    fun update(postion: Int) {
        notifyItemChanged(postion)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (data[position].sura?.nameArabic != null) 1 else 0
    }


    inner class JuzHolder(val viewBinding: JuzItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: JuzSurah) {
            viewBinding.juz = item
            viewBinding.executePendingBindings()
            viewBinding.downloadImg.setOnClickListener {
                onJuzDownload(
                    item,
                    absoluteAdapterPosition
                )
            }
        }

    }

    inner class SuraHolder(val viewBinding: SuraItemViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: JuzSurah) {
            viewBinding.juz = item
            viewBinding.executePendingBindings()
            viewBinding.root.setOnClickListener { onSurahClick(item) }
            viewBinding.downloadImg.setOnClickListener{onSurahDownload(item,absoluteAdapterPosition)}
        }

    }

}