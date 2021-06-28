package com.longjunhao.wanjetpack.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.data.home.ApiBanner
import com.longjunhao.wanjetpack.databinding.ListItemBannerBinding

/**
 * .HomeBannerViewPageAdapter
 *
 * @author Admitor
 * @date 2021/06/23
 */
class HomeBannerAdapter: RecyclerView.Adapter<BannerViewHolder>() {

    private var bannerList: List<ApiBanner> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ListItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    /**
     * 可以不需要 notifyDataSetChanged()，但是HomeFirstAdapter中的setBannerList()需要 notifyDataSetChanged(),todo： why?
     */
    fun setBannerList(list: List<ApiBanner>) {
        bannerList = list
    }

}

class BannerViewHolder(
    val binding: ListItemBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ApiBanner) {
        binding.banner = item
        binding.executePendingBindings()
    }

}