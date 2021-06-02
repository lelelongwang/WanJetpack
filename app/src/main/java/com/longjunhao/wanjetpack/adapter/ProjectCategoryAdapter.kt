package com.longjunhao.wanjetpack.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import com.longjunhao.wanjetpack.databinding.ListItemProjectCategoryBinding
import com.longjunhao.wanjetpack.viewmodels.ProjectViewModel

/**
 * .ProjectCategoryAdapter
 *
 * @author Admitor
 * @date 2021/05/31
 */
class ProjectCategoryAdapter(
    val viewModel: ProjectViewModel
) : ListAdapter<ProjectCategory, RecyclerView.ViewHolder>(ProjectCategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProjectCategoryViewHolder(
            ListItemProjectCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ProjectCategoryViewHolder).bind(item)
        holder.binding.setClickListener {
            holder.binding.projectCategory?.let {
                Log.d("TAG", "onBindViewHolder setClickListener: ljh 22 item = ${it.name}+id=${it.id}")
                viewModel.currentSelectedItem.postValue(it)
            }
        }
    }

    class ProjectCategoryViewHolder(
        val binding: ListItemProjectCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            //处理点击事件，为了设置
            binding.setClickListener {
                binding.projectCategory?.let {
                    Log.d("TAG", "ProjectCategoryViewHolder setClickListener ljh 11 : item = ${it.name}+id=${it.id}")
                }
            }
        }

        fun bind(item: ProjectCategory) {
            binding.apply {
                projectCategory = item
                executePendingBindings()
            }
        }
    }
}

private class ProjectCategoryDiffCallback : DiffUtil.ItemCallback<ProjectCategory>() {

    override fun areItemsTheSame(oldItem: ProjectCategory, newItem: ProjectCategory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProjectCategory, newItem: ProjectCategory): Boolean {
        return oldItem == newItem
    }
}