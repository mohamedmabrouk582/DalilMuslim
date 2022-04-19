package com.mabrouk.core.utils

import android.text.Html
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class DataBindAdapterUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("app:loadImage")
        fun loadImages(view: ImageView, url: String?) {
            url?.apply {
                Glide.with(view)
                    .load(url)
                    .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("app:loadImage")
        fun loadImages(view: ShapeableImageView, url: String?) {
            url?.apply {
                Glide.with(view)
                    .load(url)
                    .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("app:loadImageResource")
        fun loadImage(view: ImageView, resource: Int) {
            view.setImageResource(resource)
        }

        @JvmStatic
        @BindingAdapter("app:fromHtml")
        fun fromHtml(view: TextView, txt: String) {
            view.text =
                Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT)
        }

        @JvmStatic
        @BindingAdapter("app:searchListener")
        fun searchListener(searchView: SearchView, listener : SearchView.OnQueryTextListener){
            searchView.setOnQueryTextListener(listener)
        }

    }
}