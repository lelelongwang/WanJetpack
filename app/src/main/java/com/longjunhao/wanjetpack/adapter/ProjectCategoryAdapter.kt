package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import com.longjunhao.wanjetpack.databinding.ListItemProjectCategoryBinding

/**
 * .ProjectCategoryAdapter
 *
 * @author Admitor
 * @date 2021/05/31
 */
class ProjectCategoryAdapter(
    private val categoryOnClick: (ProjectCategory) -> Unit
) : ListAdapter<ProjectCategory, RecyclerView.ViewHolder>(ProjectCategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProjectCategoryViewHolder(
            ListItemProjectCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            categoryOnClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ProjectCategoryViewHolder).bind(item)
    }

    class ProjectCategoryViewHolder(
        val binding: ListItemProjectCategoryBinding,
        val categoryOnClick: (ProjectCategory) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.projectCategory?.let {
                    categoryOnClick(it)
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