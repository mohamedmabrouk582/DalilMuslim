package com.mabrouk.hadith_feature.presentaion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabrouk.hadith_feature.R
import com.mabrouk.hadith_feature.databinding.HadithLayoutBinding
import com.mabrouk.hadith_feature.domain.models.PassHadithKeys
import com.mabrouk.hadith_feature.presentaion.COLLECTION_NAME
import com.mabrouk.hadith_feature.presentaion.states.HadithStates
import com.mabrouk.hadith_feature.presentaion.ui.adapters.HadithBookAdapter
import com.mabrouk.hadith_feature.presentaion.viewmodels.HadithViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/24/22
 */
@AndroidEntryPoint
class HadithBookBottomSheet : BottomSheetDialogFragment() {
    private lateinit var viewBinding: HadithLayoutBinding
    val viewModel: HadithViewModel by viewModels()
    private val adapter by lazy {
        HadithBookAdapter {
            HadithDetailBottomSheet.start(PassHadithKeys(it.collectionName, it.bookNumber))
                .show(childFragmentManager, "HadithDetailBottomSheet")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.hadith_layout, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.vm = viewModel
        viewBinding.rcv.adapter = adapter
        viewBinding.rcv.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        arguments?.getString(COLLECTION_NAME)?.let {
            viewModel.loadHadithBook(it)
        }
        handleStates()
    }

    private fun handleStates() {
        lifecycleScope.launch {
            viewModel.states.collect {
                if (it is HadithStates.LoadHadithBooks) {
                    adapter.data = it.data
                }
            }
        }
    }

    companion object {
        fun start(collectionName: String): HadithBookBottomSheet {
            val bundle = Bundle()
            bundle.putString(COLLECTION_NAME, collectionName)
            val fragment = HadithBookBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }

}