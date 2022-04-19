package com.mabrouk.quran_listing_feature.presentation.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.AyaTranslateLayoutBinding
import com.mabrouk.quran_listing_feature.domain.models.Translation
import com.mabrouk.quran_listing_feature.presentation.utils.*

class AyaTranslateFragment : BottomSheetDialogFragment() {
    lateinit var viewBinding : AyaTranslateLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding=DataBindingUtil.inflate(inflater, R.layout.aya_translate_layout,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(AYA_CONTENT)?.let {
            viewBinding.txt.text= it
        }
        arguments?.getParcelableArrayList<Translation>(AYA_TRANSLATE)?.let{
            it.fold("") { acc, translationEntity ->
                acc + " " + translationEntity.textTranslation
            }.apply {
                viewBinding.trans=this
            }
        }
    }

    companion object{
        fun start(item:ArrayList<Translation>,content:String) : AyaTranslateFragment{
            val bundle =Bundle()
             bundle.putParcelableArrayList(AYA_TRANSLATE,item)
            bundle.putString(AYA_CONTENT,content)
            val fragment = AyaTranslateFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}