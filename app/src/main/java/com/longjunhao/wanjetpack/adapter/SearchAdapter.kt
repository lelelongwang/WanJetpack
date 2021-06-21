package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.SearchAdapter.ArticleViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.ListItemArticleBinding
import com.longjunhao.wanjetpack.viewmodels.SearchViewModel

/**
 * .ArticleAdapter
 *
 * @author Admitor
 * @date 2021/06/21
 */
class SearchAdapter(
    private val viewModel: SearchViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : PagingDataAdapter<ApiArticle, ArticleViewHolder>(ArticleDiffCallback()) {

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
            holder.binding.favorite.setOnClickListener { view ->
                if (article.collect) {
                    viewModel.unCollect(article.id).observe(viewLifecycleOwner, Observer {
                        if (it.errorCode == 0) {
                            article.collect = false
                            holder.binding.favorite.setImageResource(R.drawable.ic_favorite_border_24)
                            Snackbar.make(view, "取消收藏成功", Snackbar.LENGTH_LONG).show()
                        } else if (it.errorCode == -1001) {
                            //没有登录的话，collect为false，故下面的代码应该不会执行。
                            Snackbar.make(view, "未知的场景，请提bug", Snackbar.LENGTH_LONG).show()
                        }
                    })
                } else {
                    viewModel.collect(article.id).observe(viewLifecycleOwner, Observer {
                        if (it.errorCode == 0) {
                            article.collect = true
                            holder.binding.favorite.setImageResource(R.drawable.ic_favorite_24)
                            Snackbar.make(view, "收藏成功", Snackbar.LENGTH_LONG).show()
                        } else if (it.errorCode == -1001) {
                            view.findNavController().navigate(R.id.loginFragment)
                        }
                    })
                }
            }
        }
    }

    class ArticleViewHolder(
        val binding: ListItemArticleBinding
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

        fun bind(item: ApiArticle) {
            binding.apply {
                article = item
                executePendingBindings()
            }
        }

    }

    private class ArticleDiffCallback : DiffUtil.ItemCallback<ApiArticle>() {
        override fun areItemsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem == newItem
        }

    }
}