package com.mabrouk.history_feature.peresntaion.view

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.dynamicfeatures.Constants
import com.mabrouk.core.network.CheckNetwork
import com.mabrouk.history_feature.R
import com.mabrouk.history_feature.databinding.StoryFragmentBinding
import com.mabrouk.history_feature.domain.models.Story
import com.mabrouk.history_feature.peresntaion.states.StoryStates
import com.mabrouk.history_feature.peresntaion.viewmodels.StoriesViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/20/22
 */
@AndroidEntryPoint
class StoryFragment : Fragment(R.layout.story_fragment) {

    lateinit var viewBinding: StoryFragmentBinding
    val viewModel: StoriesViewModel by viewModels()
    var selectedItem: Story? = null
    var youTubePlayer: YouTubePlayer? = null
    companion object{
        val sourceRectHint = Rect()
    }


    private val adapter by lazy {
        StoryAdapter { item, _ ->
            selectedItem = item
            viewBinding.playing = true
            viewBinding.story = item
            youTubePlayer?.apply {
                this.loadVideo(item.videoKey, 0f)
            }
            if (!CheckNetwork.isOnline(requireContext())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setVideoKey() {
        viewBinding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@StoryFragment.youTubePlayer = youTubePlayer
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.videosRcv.adapter = adapter
        viewModel.loadStories()
        lifecycle.addObserver(viewBinding.playerView)
        setVideoKey()
        handleStates()
        viewBinding.playerView.getGlobalVisibleRect(sourceRectHint)
        viewBinding.downloadVideo.setOnClickListener {
            viewModel.downloadVideo(selectedItem!!)
        }
    }

    private fun handleStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.states.collect {
                when (it) {
                    is StoryStates.AddStory -> {
                        adapter.addItem(it.storyEntity)
                        viewBinding.loader.visibility = View.GONE
                    }
                    is StoryStates.Error -> Toast.makeText(
                        requireContext(),
                        it.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    is StoryStates.DownloadVideo -> {
                        it.workInfo.observe(viewLifecycleOwner) { info ->
                            Toast.makeText(requireContext(), info.state.name, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else -> {
                        Log.d(Constants.DESTINATION_ARGS , it.toString())
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewBinding.playerView.release()
    }

}