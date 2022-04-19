package com.mabrouk.quran_listing_feature.presentation.views.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.google.android.exoplayer2.*
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mabrouk.core.network.loader
import com.mabrouk.core.utils.FileUtils
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
class SurahFragment : Fragment(R.layout.surah_fragment_layout), Player.Listener,
    MultiplePermissionsListener {
    lateinit var viewBinding: SurahFragmentLayoutBinding
    private val player by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val loader by lazy { requireContext().loader(scope = lifecycleScope) }
    var currentPosition: Int = 0
    var lastPosition: Int = 0
    var sura: Surah? = null
    private val surahViewModel: SurahViewModel by viewModels()
    private val quranViewModel: QuranViewModel by viewModels()
    var playbackPosition: Long = 0
    private val adapter: AyaAdapter by lazy { AyaAdapter(::onAyaClick) }
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
        DataBindingUtil.bind<SurahFragmentLayoutBinding>(view)?.apply { viewBinding = this }
        hideAudio()
        viewBinding.ayatRcv.adapter = adapter
        initPlayer()
        viewBinding.isPlaying = player.isPlaying
        arguments?.getParcelable<Surah>(VERSES_LIST)?.apply {
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

    private fun actions() {

        viewBinding.player.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
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
            player.seekTo(verse.verse_number - if (sura?.bismillah_pre!!) -1 else 0, C.TIME_UNSET)
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
                if (verse.text_madani.isNullOrEmpty()) verse.text_indopak
                    ?: "" else verse.text_madani
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
            surahViewModel.getTafsier(verse?.chapter_id!!, verse.verse_number).first().let {
                val bundle = Bundle()
                bundle.putParcelable(AYA_TAFSIRS, AyaTafsirs(verse.text_madani!!, ArrayList(it)))
                try {
                    findNavController().navigate(
                        R.id.action_surahFragment_to_tafsirFragment, bundle
                    )
                } catch (e: Exception) {
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
            val data = quranViewModel.fetchVerse(arguments?.getInt(VERSES_ID) ?: 0).first()
            viewBinding.loaderView.visibility = View.GONE
            if (data.isNullOrEmpty()) return@launch
            adapter.verses = ArrayList(data)
            checkPermission()
            handleStates()
        }
    }

    private fun checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val permissionIntent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(permissionIntent)
            }
        }

        surahViewModel.getReader {
            if (it.versesIds != null && it.versesIds?.contains(sura?.id?.toLong()) == true) {
                prepareAudios()
            } else {
                Dexter.withContext(requireContext())
                    .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .withListener(this)
                    .check()
            }
        }

    }

    private fun handleStates() {
        lifecycleScope.launchWhenStarted {
            surahViewModel.states.collect {
                when (it) {
                    is SurahStates.DownloadVerse -> downloadVerse(it.workInfo)
                    SurahStates.IDLE -> {}
                    is SurahStates.UpdateReader -> {
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
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadVerse(workInfo: LiveData<WorkInfo>) {
        workInfo.observe(viewLifecycleOwner) { info ->
            if (info.state == WorkInfo.State.SUCCEEDED) {
                loader.dismiss()
                prepareAudios()
                if (index < data.size) {
                    surahViewModel.downloadVerseAudio(
                        requireContext(),
                        sura?.name_arabic!!,
                        ArrayList(data[index])
                    )
                    index += 1
                } else {
                    sura?.audiosDownloaded = true
                    surahViewModel.updateSurah(sura!!)
                    surahViewModel.updateReader(sura?.id?.toLong() ?: 0L)
                }
            } else if (info.state == WorkInfo.State.FAILED) {
                val error = info.outputData.getString(AUDIO_DOWNLOAD)
                if (!error.isNullOrBlank()) {
                    Snackbar.make(viewBinding.root, error, Snackbar.LENGTH_SHORT).show()
                }
                loader.dismiss()
            }
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
        player.addMediaItem(
            addMediaItem(
                FileUtils.getAudioPath(
                    surahViewModel.currentReader.sufix,
                    1,
                    0
                ), "-1"
            )
        )
        if (sura?.bismillah_pre!!)
            player.addMediaItem(
                addMediaItem(
                    FileUtils.getAudioPath(
                        surahViewModel.currentReader.sufix,
                        1,
                        1
                    ), "-1"
                )
            )
        player.addMediaItems(adapter.verses.map {
            addMediaItem(
                FileUtils.getAudioPath(
                    surahViewModel.currentReader.sufix,
                    it.chapter_id,
                    it.verse_number
                ), "${it.verse_number - 1}"
            )
        })
        // player.addMediaItem(MediaItem.fromUri("http://www.liveradiu.com/2018/06/holy-quran-radio-station-cairo-live.html"))
        player.prepare()
    }

    private fun addMediaItem(path: String, id: String): MediaItem {
        return MediaItem.Builder().setUri(Uri.parse(path)).setMediaId(id).build()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        report?.let {
            if (report.areAllPermissionsGranted()) {
                loader.show()
                surahViewModel.downloadVerseAudio(
                    requireContext(),
                    sura?.name_arabic!!,
                    arrayListOf(adapter.verses.first())
                )
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        p1: PermissionToken?
    ) {
        p1?.continuePermissionRequest()
    }


    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        // super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.mediaId?.toInt()?.run {
            if (this >= 0) {
                currentPosition = mediaItem.mediaId.toInt()
                adapter.updateVerse(currentPosition, lastPosition)
                viewBinding.scroll.post {
                    try {
                        val pos =
                            (viewBinding.ayatRcv.layoutManager!! as LinearLayoutManager).findFirstVisibleItemPosition()
                        if (currentPosition >= pos) {
                            viewBinding.scroll.smoothScrollTo(
                                0,
                                viewBinding.ayatRcv.getChildAt(currentPosition).y.toInt()
                            )
                        }
                    } catch (e: Exception) {
                    }
                }
                lastPosition = currentPosition

                if (mediaItem.mediaId.toInt() == adapter.verses.size){
                    player.stop()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackPosition = player.currentPosition
        player.release()
    }


}