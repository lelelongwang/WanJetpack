package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.databinding.ListItemProjectBinding
import com.longjunhao.wanjetpack.adapter.ProjectAdapter.ProjectViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle

/**
 * .ProjectAdapter
 *
 * @author Admitor
 * @date 2021/05/31
 */
class ProjectAdapter(
    private val favoriteOnClick: (ApiArticle) -> Unit
) : PagingDataAdapter<ApiArticle, ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            ListItemProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            favoriteOnClick
        )
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class ProjectViewHolder(
        val binding: ListItemProjectBinding,
        val favoriteOnClick: (ApiArticle) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.favorite.setOnClickListener {
                binding.project?.let {
                    favoriteOnClick(it)
                }
            }
        }

        fun bind(item: ApiArticle) {
            binding.apply {
                project = item
                executePendingBindings()
            }
        }
    }
}

private class ProjectDiffCallback : DiffUtil.ItemCallback<ApiArticle>(){
    override fun areItemsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ApiArticle, newItem: ApiArticle): Boolean {
        return oldItem == newItem
    }
}