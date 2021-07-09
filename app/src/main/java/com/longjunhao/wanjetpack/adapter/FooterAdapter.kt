package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.ListItemFooterBinding

/**
 * .FooterAdapter
 *
 * @author Admitor
 * @date 2021/06/30
 */
class FooterAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FooterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        FooterViewHolder(
            ListItemFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            retry
        )

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    /**
     * 1. 如果直接返回true的话，首次进入界面加载时，会执行两次网络请求，且RecyclerView会向下滚动一段距离。
     *    所以需要加上loadState.endOfPaginationReached= true的条件。
     *
     * 2. 注意is的优先级比||、 &&的优先级高，所以：
     *    a is b && c 是 (a is b) && c 的意思
     *    a is b || c 是 (a is b) || c 的意思
     *
     */
    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading
                || loadState is LoadState.Error
                || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }
}

class FooterViewHolder(
    val binding: ListItemFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retry.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        binding.apply {

            if (loadState is LoadState.Error) {
                msg.text = loadState.error.localizedMessage
            } else if (loadState.endOfPaginationReached) {
                msg.text = binding.root.resources.getString(R.string.load_end)
            }

            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
            msg.isVisible = loadState is LoadState.Error || loadState.endOfPaginationReached
        }

    }
}