package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.adapter.WendaAdapter.WendaViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.ListItemWendaBinding

/**
 * .WendaAdapter
 *
 * @author Admitor
 * @date 2021/05/25
 */
class WendaAdapter(
    private val favoriteOnClick: (ApiArticle, Int) -> Unit
) : PagingDataAdapter<ApiArticle, WendaViewHolder>(WendaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WendaViewHolder {
        return WendaViewHolder(
            ListItemWendaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WendaViewHolder, position: Int) {
        val wenda = getItem(position)
        if (wenda != null) {
            holder.bind(wenda)
            holder.binding.favorite.setOnClickListener {
                holder.binding.wenda?.let {
                    favoriteOnClick(it, position)
                }
            }
        }
    }

    class WendaViewHolder(
        val binding: ListItemWendaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ApiArticle) {
            binding.apply {
                wenda = item
                executePendingBindings()
            }
        }

    }

    private class WendaDiffCallback : DiffUtil.ItemCallback<ApiArticle>() {
        override fun areItemsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
            return oldItem == newItem
        }

    }
}