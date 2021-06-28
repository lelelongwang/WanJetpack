package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.CollectionArticleAdapter.ApiArticleViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.ListItemCollectionBinding

/**
 * .ArticleAdapter
 *
 * @author Admitor
 * @date 2021/06/11
 */
class CollectionArticleAdapter : PagingDataAdapter<ApiArticle, ApiArticleViewHolder>(ApiArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiArticleViewHolder {
        return ApiArticleViewHolder(
            ListItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ApiArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    class ApiArticleViewHolder(
        private val binding: ListItemCollectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.article?.let { homeArticle ->
                    val bundle = bundleOf("link" to homeArticle.link, "title" to homeArticle.title)
                    it.findNavController().navigate(R.id.webFragment, bundle)
                }
            }
        }

        fun bind(item: ApiArticle) {
            binding.apply {
                article = item
                executePendingBindings()
            }
        }

    }

    private class ApiArticleDiffCallback : DiffUtil.ItemCallback<ApiArticle>() {
        override fun areItemsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem == newItem
        }

    }
}