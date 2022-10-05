package com.mabrouk.quran_listing_feature.presentation.views.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.dynamicfeatures.Constants
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.mabrouk.core.network.loader
import com.mabrouk.core.utils.EventBus
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.QuranFragmentLayoutBinding
import com.mabrouk.quran_listing_feature.domain.models.Juz
import com.mabrouk.quran_listing_feature.domain.models.JuzSurah
import com.mabrouk.quran_listing_feature.presentation.utils.*
import com.mabrouk.quran_listing_feature.presentation.states.QuranStates
import com.mabrouk.quran_listing_feature.presentation.viewmodels.QuranViewModel
import com.mabrouk.quran_listing_feature.presentation.views.adapters.QuranAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuranFragment : Fragment() {
    lateinit var viewBinding: QuranFragmentLayoutBinding
    private val viewModel: QuranViewModel by activityViewModels()

    @Inject
    lateinit var eventBus: EventBus
    private val adapter by lazy { QuranAdapter(::onJuzDownload, ::onSurahClick) }
    private val loader by lazy { requireContext().loader(eventBus.receiveType(), lifecycleScope) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.quran_fragment_layout, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel
        viewBinding.quranRcv.adapter = adapter
        viewModel.loadData()
        handleQuranStates()
    }

    private fun handleQuranStates() {
        lifecycleScope.launch {
            viewModel.quranStates.collect {
                when (it) {
                    is QuranStates.Error -> {
                        Log.e("Error",it.error)
                    }
                    is QuranStates.LoadJuzSurahs -> {
                        viewModel.surahListDownloads()
                        adapter.data = it.juzSurah
                    }
                    is QuranStates.SearchResult -> {
                        Toast.makeText(requireContext(), it.query, Toast.LENGTH_SHORT).show()
                        Log.e("Error",it.query)
                    }
                    else -> {
                        Log.d("TAG","")
                    }
                }
            }
        }
    }

    private fun onSurahClick(juzSurah: JuzSurah) {
        lifecycleScope.launch {
            val value = viewModel.fetchVerse(juzSurah.sura?.id!!).first()
            if (value.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.sourah_not_download),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val bundle = Bundle()
                bundle.putInt(VERSES_ID, juzSurah.sura.id)
                bundle.putParcelable(VERSES_LIST, juzSurah.sura)
                findNavController()
                    .navigate(R.id.action_quranFragment_to_surahFragment, bundle)
            }
        }
    }

    private fun onJuzDownload(item: JuzSurah, postion: Int) {
        loader.show()
        val ids = item.verseIds.filter { verseId -> !viewModel.surahs[verseId - 1].isDownload }
        viewModel.downloadJuz(ids, requireContext()).observe(this) {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                lifecycleScope.launch {
                    item.isDownload = true
                    adapter.update(postion)
                    ids.forEach { suraId ->
                        viewModel.updateSurah(viewModel.surahs[suraId - 1].apply {
                            isDownload = true
                        })
                    }
                    viewModel.updateJuz(Juz(item.juzNum, item.juzNum, item.verseMap, true))
                    loader.dismiss()
                }
            } else if (it.state == WorkInfo.State.FAILED) {
                loader.dismiss()
            } else {
                Log.d(Constants.DESTINATION_ARGS , it.state.name)
            }
        }

    }
}