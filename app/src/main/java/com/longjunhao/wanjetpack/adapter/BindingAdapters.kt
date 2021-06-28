/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.longjunhao.wanjetpack.adapter

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.home.ApiBanner

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("webViewFromArticle")
fun bindWebViewFromArticle(view: View, article: Any) {
    view.setOnClickListener {
        when (article) {
            is ApiArticle -> {
                article.let {
                    val bundle = bundleOf("link" to it.link, "title" to it.title)
                    view.findNavController().navigate(R.id.webFragment, bundle)
                }
            }
            is ApiBanner -> {
                article.let {
                    val bundle = bundleOf("link" to article.url, "title" to article.title)
                    view.findNavController().navigate(R.id.webFragment, bundle)
                }
            }
            else -> Snackbar.make(view, "未知的数据类型...", Snackbar.LENGTH_LONG).show()
        }
    }
}

/**
 * todo 点击搜索时，也期望通过BindingAdapter实现，有flow，暂时没有实现。和“项目”里的点击项目分类里的原因类似，都是有
 *   flow，由于有flow导致在点击项目分类时，用协程构造器失败。
 */
@BindingAdapter(value = ["searchAction"])
fun bindSearch(editText: EditText, callback: () -> Unit) {
    editText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Log.d("bindingAdapters", "bindSearch: ")
            callback()
        }
        true
    }

}
