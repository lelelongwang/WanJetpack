package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.ListItemProjectBinding
import com.longjunhao.wanjetpack.adapter.ProjectAdapter.ProjectViewHolder
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.viewmodels.ProjectViewModel

/**
 * .ProjectAdapter
 *
 * @author Admitor
 * @date 2021/05/31
 */
class ProjectAdapter(
    private val viewModel: ProjectViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : PagingDataAdapter<ApiArticle, ProjectViewHolder>(ProjectDiffCallback()) {

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
            holder.binding.favorite.setOnClickListener { view ->
                if (item.collect) {
                    viewModel.unCollect(item.id).observe(viewLifecycleOwner, Observer {
                        if (it.errorCode == 0) {
                            item.collect = false
                            holder.binding.favorite.setImageResource(R.drawable.ic_favorite_border_24)
                            Snackbar.make(view, "取消收藏成功", Snackbar.LENGTH_LONG).show()
                        } else if (it.errorCode == -1001) {
                            Snackbar.make(view, "请先登录账号，待实现", Snackbar.LENGTH_LONG).show()
                        }
                    })
                } else {
                    viewModel.collect(item.id).observe(viewLifecycleOwner, Observer {
                        if (it.errorCode == 0) {
                            item.collect = true
                            holder.binding.favorite.setImageResource(R.drawable.ic_favorite_24)
                            Snackbar.make(view, "收藏成功", Snackbar.LENGTH_LONG).show()
                        } else if (it.errorCode == -1001) {
                            Snackbar.make(view, "请先登录账号，待实现", Snackbar.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
    }

    class ProjectViewHolder(
        val binding: ListItemProjectBinding
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