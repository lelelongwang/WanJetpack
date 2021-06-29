package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.adapter.WechatAdapter.WechatViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.ListItemWechatBinding

/**
 * .WechatAdapter
 *
 * @author Admitor
 * @date 2021/05/28
 */
class WechatAdapter(
    private val favoriteOnClick: (ApiArticle, Int) -> Unit
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
            holder.binding.favorite.setOnClickListener {
                holder.binding.wechat?.let {
                    favoriteOnClick(it, position)
                }
            }
        }
    }

    class WechatViewHolder(
        val binding: ListItemWechatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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