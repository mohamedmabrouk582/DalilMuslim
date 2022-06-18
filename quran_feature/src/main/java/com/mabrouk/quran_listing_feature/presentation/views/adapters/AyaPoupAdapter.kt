package com.mabrouk.quran_listing_feature.presentation.views.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.AyaOpationsLayoutBinding
import com.mabrouk.quran_listing_feature.domain.models.Verse

class AyaPoupAdapter(
    val onPlayClick: (verse: Verse?) -> Unit,
    val onTranlationClick: (verse: Verse?) -> Unit,
    val onTafsirClick: (verse: Verse?) -> Unit
) : BaseAdapter() {
    var verse: Verse? = null
    override fun getCount(): Int = 1

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = DataBindingUtil.inflate<AyaOpationsLayoutBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.aya_opations_layout, parent, false
        )
        view.playImg.setOnClickListener { onPlayClick(verse) }
        view.translationImg.setOnClickListener { onTranlationClick(verse) }
        view.tafsirImg.setOnClickListener { onTafsirClick(verse) }
        //view.youtube.setOnClickListener { listener.OnYoutClick(verse) }
        return view.root
    }
}