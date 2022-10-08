package com.mabrouk.hadith_feature.presentaion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabrouk.hadith_feature.R
import com.mabrouk.hadith_feature.databinding.HadithLayoutBinding
import com.mabrouk.hadith_feature.domain.models.PassHadithKeys
import com.mabrouk.hadith_feature.presentaion.HADITH_KEYS
import com.mabrouk.hadith_feature.presentaion.states.HadithStates
import com.mabrouk.hadith_feature.presentaion.ui.adapters.HadithAdapter
import com.mabrouk.hadith_feature.presentaion.viewmodels.HadithViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@AndroidEntryPoint
class HadithDetailBottomSheet : BottomSheetDialogFragment() {
    lateinit var viewBinding: HadithLayoutBinding
    private val viewModel : HadithViewModel by viewModels()
    private val adapter by lazy { HadithAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.hadith_layout,container,false)
      return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm=viewModel
        viewBinding.rcv.adapter=adapter
        handleStates()
        arguments?.getParcelable<PassHadithKeys>(HADITH_KEYS)?.let {
            viewModel.loadHadiths(it.name,it.bookNumber)
        }
    }

    private fun handleStates() {
        lifecycleScope.launch {
            viewModel.states.collect {
                if (it is HadithStates.LoadHadith){
                    adapter.setValues(it.data)
                }
            }
        }
    }

    companion object{
        fun start(item:PassHadithKeys) : HadithDetailBottomSheet{
            val bundle =Bundle()
            bundle.putParcelable(HADITH_KEYS,item)
            val fragment = HadithDetailBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }
}