package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.WechatAdapter.WechatViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.ListItemWechatBinding
import com.longjunhao.wanjetpack.viewmodels.WechatArticleViewModel

/**
 * .WechatAdapter
 *
 * @author Admitor
 * @date 2021/05/28
 */
class WechatAdapter(
    private val viewModel: WechatArticleViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : PagingDataAdapter<ApiArticle, WechatViewHolder>(WechatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WechatViewHolder {
        return WechatViewHolder(
            ListItemWechatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WechatViewHolder, position: Int) {
        val wechat = getItem(position)
        if (wechat != null) {
            holder.bind(wechat)
            holder.binding.favorite.setOnClickListener { view ->
                if (wechat.collect) {
                    viewModel.unCollect(wechat.id).observe(viewLifecycleOwner, Observer {
                        if (it.errorCode == 0) {
                            wechat.collect = false
                            holder.binding.favorite.setImageResource(R.drawable.ic_favorite_border_24)
                            Snackbar.make(view, "取消收藏成功", Snackbar.LENGTH_LONG).show()
                        } else if (it.errorCode == -1001) {
                            Snackbar.make(view, "未知的场景，请提bug", Snackbar.LENGTH_LONG).show()
                        }
                    })
                } else {
                    viewModel.collect(wechat.id).observe(viewLifecycleOwner, Observer {
                        if (it.errorCode == 0) {
                            wechat.collect = true
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

    class WechatViewHolder(
        val binding: ListItemWechatBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.wechat?.let { wechat ->
                    val bundle = bundleOf("link" to wechat.link, "title" to wechat.title)
                    it.findNavController().navigate(R.id.webFragment, bundle)
                }
            }
        }

        fun bind(item: ApiArticle) {
            binding.apply {
                wechat = item
                executePendingBindings()
            }
        }

    }

    private class WechatDiffCallback : DiffUtil.ItemCallback<ApiArticle>() {
        override fun areItemsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem == newItem
        }

    }
}