package com.mabrouk.azkar_feature.presentaion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabrouk.azkar_feature.R
import com.mabrouk.azkar_feature.databinding.AzqarFragmentLayoutBinding
import com.mabrouk.azkar_feature.presentaion.ui.adapters.AzkarAdapter
import com.mabrouk.azkar_feature.presentaion.ui.fragments.AzqarFragment.Companion.AZKAR_CATEGORY
import com.mabrouk.azkar_feature.presentaion.viewmodels.AzkarViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@AndroidEntryPoint
class AzkarDetailsBottomSheet : BottomSheetDialogFragment() {
    lateinit var viewBinding : AzqarFragmentLayoutBinding
    val adapter by lazy {AzkarAdapter()}
    val viewModel : AzkarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.azqar_fragment_layout,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.azkarRcv.adapter = adapter

        arguments?.getString(AZKAR_CATEGORY)?.let {
            lifecycleScope.launchWhenStarted {
                adapter.data = viewModel.getAzkar(it)
            }
        }

    }

    companion object{
        fun start( item:String) : AzkarDetailsBottomSheet{
            val bundle =Bundle()
            bundle.putString(AZKAR_CATEGORY,item)
            val fragment = AzkarDetailsBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }
}