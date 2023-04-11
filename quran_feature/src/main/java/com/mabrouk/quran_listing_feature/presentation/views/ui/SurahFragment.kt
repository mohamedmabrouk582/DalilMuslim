package com.mabrouk.quran_listing_feature.presentation.views.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ListPopupWindow
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.dynamicfeatures.Constants
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.google.android.exoplayer2.*
import com.google.android.material.snackbar.Snackbar
import com.mabrouk.core.network.loader
import com.mabrouk.core.utils.FileUtils
import com.mabrouk.core.utils.PrayerSounds
import com.mabrouk.quran_listing_feature.R
import com.mabrouk.quran_listing_feature.databinding.SurahFragmentLayoutBinding
import com.mabrouk.quran_listing_feature.domain.models.AyaTafsirs
import com.mabrouk.quran_listing_feature.domain.models.Surah
import com.mabrouk.quran_listing_feature.domain.models.Verse
import com.mabrouk.quran_listing_feature.presentation.states.SurahStates
import com.mabrouk.quran_listing_feature.presentation.utils.*
import com.mabrouk.quran_listing_feature.presentation.viewmodels.QuranViewModel
import com.mabrouk.quran_listing_feature.presentation.viewmodels.SurahViewModel
import com.mabrouk.quran_listing_feature.presentation.views.adapters.AyaAdapter
import com.mabrouk.quran_listing_feature.presentation.views.adapters.AyaPoupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/17/22
 */
@AndroidEntryPoint
class SurahFragment : Fragment(R.layout.surah_fragment_layout), Player.Listener {
    lateinit var viewBinding: SurahFragmentLayoutBinding
    private val player by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val loader by lazy { requireContext().loader(scope = lifecycleScope) }
    var currentPosition: Int = 0
    var lastPosition: Int = 0
    var sura: Surah? = null
    private val surahViewModel: SurahViewModel by viewModels()
    private val quranViewModel: QuranViewModel by activityViewModels()
    var playbackPosition: Long = 0
    private val adapter: AyaAdapter by lazy { AyaAdapter(::onAyaClick) }
    lateinit var activityForResultLauncher: ActivityResultLauncher<Intent?>
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    private val popAdapter: AyaPoupAdapter by lazy {
        AyaPoupAdapter(
            ::onPlayClick,
            ::onTranslationClick,
            ::onTafsirClick
        )
    }
    lateinit var popupWindow: ListPopupWindow
    var hideAudioButtons: Int = -1
    var index = 0
    val data by lazy {
        adapter.verses.chunked(minOf(7, adapter.verses.size - 1))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentPosition = arguments?.getInt(SURAH_INDEX) ?: 0
        activityForResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                getReaders()
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it.values.all { it }.let {
                    loader.show()
                    surahViewModel.downloadVerseAudio(
                        requireContext(),
                        sura?.nameArabic!!,
                        arrayListOf(adapter.verses.first())
                    )
                }
            }
        DataBindingUtil.bind<SurahFragmentLayoutBinding>(view)?.apply { viewBinding = this }
        hideAudio()
        viewBinding.ayatRcv.adapter = adapter
        initPlayer()
        viewBinding.isPlaying = player.isPlaying
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable(
                VERSES_LIST,
                Surah::class.java
            ) else arguments?.getParcelable(VERSES_LIST))
            ?.apply {
                viewBinding.surah = this
                this@SurahFragment.sura = this
                loadSurah()
            }


        viewBinding.scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                viewBinding.player.hide()
                viewBinding.showHide.hide()
                hideAudio()
            } else {
                viewBinding.showHide.show()
                viewBinding.player.show()
                if (hideAudioButtons == 1) showAudio()
            }
        })

        actions()
    }

    private fun initApp(surah: Surah) {
        viewBinding.isPlaying = player.isPlaying
        viewBinding.surah = surah
        this@SurahFragment.sura = surah

        loadSurah()
    }

    private fun actions() {
        viewBinding.nextSurah.setOnClickListener {
            viewBinding.nextSurah.visibility = View.GONE
            if (quranViewModel.surahs.size > sura?.id!!) {
                quranViewModel.surahs[sura?.id!!].apply {
                    reset()
                    initApp(this)
                }

            }
        }

        viewBinding.player.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
                player.seekTo(currentPosition, C.TIME_UNSET)
            }
            viewBinding.isPlaying = player.isPlaying
        }

        viewBinding.nextAudio.setOnClickListener {
            player.seekToNext()
        }

        viewBinding.prevAudio.setOnClickListener {
            player.seekToPrevious()
        }

        viewBinding.showHide.setOnClickListener {
            if (viewBinding.show == true) {
                hideAudioButtons = 0
                hideAudio()
            } else {
                hideAudioButtons = 1
                showAudio()
            }
        }


        viewBinding.readerImg.setOnClickListener {
            ReadersFragment.start(activity?.supportFragmentManager!!, surahViewModel)
        }
    }

    private fun onAyaClick(view: View, verse: Verse) {
        popupWindow = ListPopupWindow(requireContext())
        popAdapter.verse = verse
        popupWindow.setAdapter(popAdapter)
        popupWindow.width = requireContext().resources.getDimensionPixelSize(R.dimen.popup_width)
        popupWindow.height = requireContext().resources.getDimensionPixelSize(R.dimen.popup_height)
        popupWindow.setDropDownGravity(Gravity.CENTER)
        popupWindow.anchorView = view
        popupWindow.isModal = true
        popupWindow.show()
    }

    private fun onPlayClick(verse: Verse?) {
        verse?.apply {
            player.seekTo(verse.verseNumber - if (sura?.bismillahPre!!) -1 else 0, C.TIME_UNSET)
            player.prepare()
            player.play()
            popupWindow.dismiss()
            viewBinding.isPlaying = true
            showAudio()
        }
    }

    private fun onTranslationClick(verse: Verse?) {
        verse?.getTranslation()?.let {
            AyaTranslateFragment.start(
                it,
                if (verse.textMadani.isNullOrEmpty()) verse.textIndopak
                    ?: "" else verse.textMadani
            ).show(
                activity?.supportFragmentManager!!,
                ""
            )
            popupWindow.dismiss()
        }
    }

    private fun onTafsirClick(verse: Verse?) {
        popupWindow.dismiss()
        lifecycleScope.launch {
            surahViewModel.getTafsier(verse?.chapterId!!, verse.verseNumber).first().let {
                val bundle = Bundle()
                bundle.putParcelable(AYA_TAFSIRS, AyaTafsirs(verse.textMadani!!, ArrayList(it)))
                try {
                    findNavController().navigate(
                        R.id.action_surahFragment_to_tafsirFragment, bundle
                    )
                } catch (e: Exception) {
                    Log.d(Constants.DESTINATION_ARGS, e.toString())
                }
            }
        }
    }

    private fun initPlayer() {
        player.addListener(this)
        player.seekTo(playbackPosition)
    }

    private fun loadSurah() {
        lifecycleScope.launch {
            val data = quranViewModel.fetchVerse(sura?.id ?: 0).first()
            if (data.isEmpty()) {
                sura?.apply {
                    downloadSurahVerses(this)
                }
            } else {
                viewBinding.loaderView.visibility = View.GONE
                adapter.verses = ArrayList(data)
                if (currentPosition > 1) {
                    handleScrolling(1)
                    showAudio()
                }
                checkPermission()
            }
            handleStates()
        }
    }

    private fun downloadSurahVerses(surah: Surah) {
        quranViewModel.downloadJuz(listOf(surah.id), requireContext())
            .observe(viewLifecycleOwner) {
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    lifecycleScope.launch {
                        quranViewModel.updateSurah(surah.apply {
                            isDownload = true
                        })
                        loadSurah()
                    }
                }
            }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val permissionIntent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            activityForResultLauncher.launch(permissionIntent)
        }
        getReaders()
    }

    private fun getReaders() {
        surahViewModel.getReader {
            if (it.versesIds != null && it.versesIds?.contains(sura?.id?.toLong()) == true) {
                prepareAudios()
            } else {
                permissionLauncher?.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun handleStates() {
        lifecycleScope.launchWhenStarted {
            surahViewModel.states.collect {
                when (it) {
                    is SurahStates.DownloadVerse -> downloadVerse(it.workInfo)
                    is SurahStates.UpdateReader -> updateReader()
                    else -> {
                        Log.d("testing", it.toString())
                    }
                }
            }
        }
    }

    private fun updateReader() {
        player.clearMediaItems()
        initPlayer()
        playbackPosition = 0
        index = 0
        adapter.updateVerse(-1, lastPosition)
        lastPosition = 0
        if (viewBinding.isPlaying == true) player.play()
        checkPermission()
        viewBinding.scroll.post {
            try {
                val pos =
                    (viewBinding.ayatRcv.layoutManager!! as LinearLayoutManager).findFirstVisibleItemPosition()
                if (currentPosition >= pos) {
                    viewBinding.scroll.smoothScrollTo(
                        0, 0
                    )
                }
            } catch (e: Exception) {
                Log.d(Constants.DESTINATION_ARGS, e.message.toString())
            }
        }
    }

    private fun downloadVerse(workInfo: LiveData<WorkInfo>) {
        workInfo.observe(viewLifecycleOwner) { info ->
            when (info.state) {
                WorkInfo.State.SUCCEEDED -> {
                    downloadVerseSuccess()
                }
                WorkInfo.State.FAILED -> {
                    val error = info.outputData.getString(AUDIO_DOWNLOAD)
                    if (!error.isNullOrBlank()) {
                        Snackbar.make(viewBinding.root, error, Snackbar.LENGTH_SHORT).show()
                    }
                    loader.dismiss()
                }
                else -> {
                    Log.d("testing", "")
                }
            }
        }
    }

    private fun downloadVerseSuccess() {
        loader.dismiss()
        prepareAudios()
        if (index < data.size) {
            surahViewModel.downloadVerseAudio(
                requireContext(),
                sura?.nameArabic!!,
                ArrayList(data[index])
            )
            index += 1
        } else {
            sura?.audiosDownloaded = true
            surahViewModel.updateSurah(sura!!)
            surahViewModel.updateReader(sura?.id?.toLong() ?: 0L)
        }
    }

    private fun hideAudio() {
        viewBinding.show = false
        viewBinding.nextAudio.hide()
        viewBinding.prevAudio.hide()
    }

    private fun showAudio() {
        viewBinding.show = true
        viewBinding.nextAudio.show()
        viewBinding.prevAudio.show()
    }

    private fun prepareAudios() {
        if (player.mediaItemCount >= adapter.verses.size) return
        player.addMediaItem(
            addMediaItem(
                FileUtils.getMp3Path(
                    requireContext(),
                    surahViewModel.currentReader.sufix,
                    1,
                    0
                ), "-1"
            )
        )
        if (sura?.bismillahPre!!)
            player.addMediaItem(
                addMediaItem(
                    FileUtils.getMp3Path(
                        requireContext(),
                        surahViewModel.currentReader.sufix,
                        1,
                        1
                    ), "-1"
                )
            )
        player.addMediaItems(adapter.verses.map {
            val filePath = FileUtils.getMp3Path(
                requireContext(),
                surahViewModel.currentReader.sufix,
                it.chapterId,
                it.verseNumber
            )
            Log.d("FILEPATH",filePath)
            addMediaItem(
                filePath , "${it.verseNumber - 1}"
            )
        })
        player.prepare()
    }

    private fun addMediaItem(path: String, id: String): MediaItem {
        return MediaItem.Builder().setUri(Uri.parse(path)).setMediaId(id).build()
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        // super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.mediaId?.toInt()?.run {
            if (this >= 0) {
                currentPosition = mediaItem.mediaId.toInt()
                adapter.updateVerse(currentPosition, lastPosition)
                handleScrolling()
                lastPosition = currentPosition
                player.nextMediaItemIndex
            }
        }
    }

    private fun handleScrolling(subPosition: Int = 0) {
        viewBinding.scroll.post {
            try {
                val pos =
                    (viewBinding.ayatRcv.layoutManager!! as LinearLayoutManager).findFirstVisibleItemPosition()
                if (currentPosition >= pos) {
                    viewBinding.scroll.smoothScrollTo(
                        0,
                        viewBinding.ayatRcv.getChildAt(currentPosition - subPosition).y.toInt()
                    )
                }
            } catch (e: Exception) {
                Log.d(Constants.DESTINATION_ARGS, e.message ?: "")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackPosition = player.currentPosition
        player.release()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == ExoPlayer.STATE_ENDED && lastPosition > 0) {
            viewBinding.nextSurah.visibility = View.VISIBLE
            player.stop()
        }
    }

    private fun reset() {
        currentPosition = arguments?.getInt(SURAH_INDEX) ?: 0
        lastPosition = 0
        playbackPosition = 0
        adapter.verses = arrayListOf()
        player.clearMediaItems()
        viewBinding.loaderView.visibility = View.VISIBLE
    }


}