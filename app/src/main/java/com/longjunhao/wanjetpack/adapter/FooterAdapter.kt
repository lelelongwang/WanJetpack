package com.longjunhao.wanjetpack.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = FooterViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: FooterViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)
}

class FooterViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_footer, parent, false)
) {
    private val binding = ListItemFooterBinding.bind(itemView)
    private val progressBar: ProgressBar = binding.progressBar
    private val errorMsg: TextView = binding.errorMsg
    private val retry: Button = binding.retryButton
        .also {
            it.setOnClickListener { retry() }
        }

    /**
     * todo 遗留问题：当所有数据都加载后，应该在FooterView上显示“已经到底了”之类的。
     */
    fun bind(loadState: LoadState) {
        Log.d("FooterAdapter", "bind: ljh loadState=$loadState")
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = loadState is LoadState.Error
    }
}