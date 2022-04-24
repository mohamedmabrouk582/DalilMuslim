package com.mabrouk.azkar_feature.presentaion.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mabrouk.azkar_feature.R
import com.mabrouk.azkar_feature.databinding.AzqarFragmentLayoutBinding
import com.mabrouk.azkar_feature.presentaion.ui.adapters.AzkarCategoryAdapter
import com.mabrouk.azkar_feature.presentaion.viewmodels.AzkarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/20/22
 */
@AndroidEntryPoint
class AzqarFragment : Fragment(R.layout.azqar_fragment_layout) {
    lateinit var viewBinding: AzqarFragmentLayoutBinding
    val viewModel: AzkarViewModel by viewModels()
    private val adpter by lazy {
        AzkarCategoryAdapter {
          AzkarDetailsBottomSheet.start(it)
              .show(childFragmentManager,"")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.azkarRcv.adapter = adpter

        lifecycleScope.launch {
            adpter.data = viewModel.loadCategories()
        }
    }

    companion object{
        const val AZKAR_CATEGORY = "AZKAR_CATEGORY"
    }
}