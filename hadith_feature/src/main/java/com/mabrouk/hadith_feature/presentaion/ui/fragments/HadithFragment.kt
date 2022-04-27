package com.mabrouk.hadith_feature.presentaion.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mabrouk.hadith_feature.R
import com.mabrouk.hadith_feature.databinding.HadithLayoutBinding
import com.mabrouk.hadith_feature.databinding.HadithLayoutFragmentBinding
import com.mabrouk.hadith_feature.presentaion.states.HadithStates
import com.mabrouk.hadith_feature.presentaion.ui.adapters.HadithCategoryAdapter
import com.mabrouk.hadith_feature.presentaion.viewmodels.HadithViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/20/22
 */
@AndroidEntryPoint
class HadithFragment : Fragment(R.layout.hadith_layout_fragment) {
    lateinit var viewBinding: HadithLayoutFragmentBinding
    val viewModel: HadithViewModel by viewModels()
    private val adapter by lazy {
        HadithCategoryAdapter {
            HadithBookBottomSheet.start(it.name).show(requireActivity().supportFragmentManager, "HadithBookBottomSheet")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.rcv.adapter = adapter
        viewBinding.rcv.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        handleStates()
        viewModel.getHadithCategories()
    }

    private fun handleStates() {
        lifecycleScope.launch {
            viewModel.states.collect {
                if (it is HadithStates.LoadCategories) {
                    adapter.data = it.data
                }
            }
        }
    }
}