package com.mabrouk.quran_listing_feature.presentation.views.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.ReadersLayoutBinding
import com.mabrouk.quran_listing_feature.presentation.viewmodels.SurahViewModel
import com.mabrouk.quran_listing_feature.presentation.views.adapters.ReaderAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 03/09/2021 created by Just clean
 */
@AndroidEntryPoint
class ReadersFragment(val viewModel: SurahViewModel) : BottomSheetDialogFragment() {
    lateinit var viewBinding: ReadersLayoutBinding
    val adapter by lazy {
        ReaderAdapter {
             viewModel.updateReader(it)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<ReadersLayoutBinding>(
            inflater,
            R.layout.readers_layout,
            container,
            false
        ).apply {
            viewBinding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.readersRcv.adapter = adapter
        viewModel.getAllReaders {

            adapter.items = it.apply {
                find { it.readerId == viewModel.currentReader.readerId }?.isSelected=true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }


    companion object{
        fun start(fm:FragmentManager,viewModel: SurahViewModel){
            ReadersFragment(viewModel).show(fm,"ReadersFragment")
        }
    }

}