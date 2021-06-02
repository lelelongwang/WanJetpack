package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.adapter.HomeArticleAdapter.ArticleViewHolder
import com.longjunhao.wanjetpack.data.HomeArticle
import com.longjunhao.wanjetpack.databinding.ListItemArticleBinding

/**
 * .ArticleAdapter
 *
 * @author Admitor
 * @date 2021/05/21
 */
class HomeArticleAdapter : PagingDataAdapter<HomeArticle, ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ListItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    class ArticleViewHolder(
        private val binding: ListItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.article?.let { homeArticle ->
                    val uri = Uri.parse(homeArticle.link)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    it.context.startActivity(intent)
                }
            }
        }

        fun bind(item: HomeArticle) {
            binding.apply {
                article = item
                executePendingBindings()
            }
        }

    }

    private class ArticleDiffCallback : DiffUtil.ItemCallback<HomeArticle>() {
        override fun areItemsTheSame(oldItem: HomeArticle, newItem: HomeArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeArticle, newItem: HomeArticle): Boolean {
            return oldItem == newItem
        }

    }
}