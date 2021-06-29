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
    private val favoriteOnClick: (ApiArticle) -> Unit
) : PagingDataAdapter<ApiArticle, WendaViewHolder>(WendaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WendaViewHolder {
        return WendaViewHolder(
            ListItemWendaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            favoriteOnClick
        )
    }

    override fun onBindViewHolder(holder: WendaViewHolder, position: Int) {
        val wenda = getItem(position)
        if (wenda != null) {
            holder.bind(wenda)
        }
    }

    class WendaViewHolder(
        val binding: ListItemWendaBinding,
        val favoriteOnClick: (ApiArticle) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.favorite.setOnClickListener {
                binding.wenda?.let {
                    favoriteOnClick(it)
                }
            }
        }

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