package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.data.project.ProjectArticle
import com.longjunhao.wanjetpack.databinding.ListItemProjectBinding
import com.longjunhao.wanjetpack.adapter.ProjectAdapter.ProjectViewHolder

/**
 * .ProjectAdapter
 *
 * @author Admitor
 * @date 2021/05/31
 */
class ProjectAdapter : PagingDataAdapter<ProjectArticle, ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            ListItemProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class ProjectViewHolder(
        private val binding: ListItemProjectBinding
    ): RecyclerView.ViewHolder(binding.root){
        init {
            binding.setClickListener {
                binding.project?.let { projectArticle ->
                    val uri = Uri.parse(projectArticle.link)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    it.context.startActivity(intent)
                }
            }
        }

        fun bind(item: ProjectArticle) {
            binding.apply {
                project = item
                executePendingBindings()
            }
        }
    }
}

private class ProjectDiffCallback : DiffUtil.ItemCallback<ProjectArticle>(){
    override fun areItemsTheSame(oldItem: ProjectArticle, newItem: ProjectArticle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProjectArticle, newItem: ProjectArticle): Boolean {
        return oldItem == newItem
    }
}