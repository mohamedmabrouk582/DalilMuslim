package com.mabrouk.radio_quran_feature.presentaion.ui

import android.content.Context
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.mabrouk.radio_quran_feature.R
import com.mabrouk.radio_quran_feature.domain.models.Radio
import com.mabrouk.radio_quran_feature.presentaion.viewmodels.RadioStates
import com.mabrouk.radio_quran_feature.presentaion.viewmodels.RadioViewModel

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 10/19/22
 */


@Composable
fun layoutHeader(context: Context, title: String) {
    val imgLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(R.drawable.quran).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imgLoader
            ), contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.size(5.dp))

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(40.dp),
//            horizontalArrangement = Arrangement.Start
//        ) {
//
//        }
        Text(
            text = title, fontFamily = FontFamily(
                listOf(
                    Font(
                        resId = R.font.quran,
                        weight = FontWeight.ExtraBold,
                        style = FontStyle.Normal
                    )
                )
            ),
            modifier = Modifier.padding(end = 10.dp, start = 10.dp)
        )
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun listRadios(radios: ArrayList<Radio>) {
    Box {
        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(radios.size) { index ->
                radioChannel(item = radios[index])
            }
        }
    }
}

@Composable
fun radioChannel(item: Radio) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(
                RoundedCornerShape(10.dp)
            )
    ) {
        Text(text = item.name)
    }
}

@Composable
fun radioQuran(viewModel:RadioViewModel= hiltViewModel()){
    viewModel.requestRadios()

    when(val value = viewModel.states.collectAsState().value){
        is RadioStates.LoadData -> {
            Column(modifier = Modifier.fillMaxSize()) {
                layoutHeader(context = LocalContext.current, title = "testing")
                Spacer(modifier = Modifier.size(10.dp))
                listRadios(radios =
                    value.data
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun preview() {
    radioQuran()
}