package com.longjunhao.wanjetpack.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.data.home.ApiBanner
import com.longjunhao.wanjetpack.databinding.ListItemFirstBinding
import com.longjunhao.wanjetpack.views.NestedFrameLayout

/**
 * .HomeBannerAdapter
 * todo 如果只有一个banner，没有其他的控件，是否可以把HomeFirstAdapter干掉，直接用HomeBannerAdapter添加到ConcatAdapter中呢？
 *
 * @author Admitor
 * @date 2021/06/23
 */
class HomeFirstAdapter: RecyclerView.Adapter<HomeFirstAdapter.FirstViewHolder>() {

    private var bannerList: List<ApiBanner> = ArrayList()
    lateinit var viewHolder: FirstViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstViewHolder {
        viewHolder = FirstViewHolder(
            ListItemFirstBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: FirstViewHolder, position: Int) {
        holder.bind(bannerList)
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun setBannerList(list: List<ApiBanner>) {
        bannerList = list
        notifyDataSetChanged()
    }

    /**
     * 1. 要加上isInitialized判断，因为当来回切换深色主题时，viewHolder还没有初始化，
     * 就已经调用autoScrollBannerPage()导致 UninitializedPropertyAccessException 的crash
     *
     * 2. 当手指触摸在viewpager2上时，不应该自动滚动。
     */
    fun autoScrollBannerPage() {
        if (this::viewHolder.isInitialized) {
            viewHolder.binding.bannerViewPager.run {
                val isAutoScroll = !((parent as NestedFrameLayout).isTouched)
                Log.d("HomeFirstAdapter", "autoScrollBannerPage: ljh isAutoScroll=$isAutoScroll")
                if (isAutoScroll) {
                    setCurrentItem(currentItem + 1, true)
                }
            }
        }
    }

    class FirstViewHolder(
        val binding: ListItemFirstBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val adapter: HomeBannerAdapter = HomeBannerAdapter()

        init {
            binding.bannerViewPager.adapter = adapter
            binding.bannerViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                var currentPosition: Int = 0

                /**
                 * 当页面在滑动时至滑动被停止之前，此方法会一直调用onPageScrolled()。在滚动的时候进行回调，
                 * 分两种情况，一种是代码控制的滚动，还是用户手划滚动，都能监听的到，这个方法可以做一些滚动时的过程控制
                 *
                 * position :当前页面，及你点击滑动的页面
                 * positionOffset:当前页面偏移的百分比
                 * positionOffsetPixels:当前页面偏移的像素位置
                 */
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.d("FirstViewHolder", "onPageScrolled: ljh position=$position")
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                /**
                 * 页面跳转完后调用onPageSelected()，这个方法是在新页面已经呈现，并且动作还没有执行完全的时候回调的，
                 *
                 * position:当前选中页面的Position（位置编号）
                 */
                override fun onPageSelected(position: Int) {
                    Log.d("FirstViewHolder", "onPageSelected: ljh currentPosition=$currentPosition  position=$position")

                    if (position <= binding.indicatorLayout.childCount) {
                        updateIndicator(position)
                    }
                    currentPosition = position
                    super.onPageSelected(position)
                }

                /**
                 * 页面状态改变时调用onPageScrollStateChanged(),
                 * stated 的状态：
                 * SCROLL_STATE_IDLE = 0;：空闲状态
                 * SCROLL_STATE_DRAGGING = 1;：滑动状态
                 * SCROLL_STATE_SETTLING = 2;：滑动后自然沉降的状态
                 *
                 */
                override fun onPageScrollStateChanged(state: Int) {
                    Log.d("FirstViewHolder", "onPageScrollStateChanged: ljh currentPosition=$currentPosition  state=$state")
                    //只有在空闲状态，才让自动滚动
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        if (currentPosition == 0) {
                            binding.bannerViewPager.setCurrentItem(adapter.itemCount - 2, false);
                        } else if (currentPosition == adapter.itemCount - 1) {
                            binding.bannerViewPager.setCurrentItem(1, false);
                        }
                    }
                    super.onPageScrollStateChanged(state)
                }
            })
        }

        fun bind(item: List<ApiBanner>) {
            Log.d("FirstViewHolder", "bind: ljhwlp  item.size="+item.size)
            item.let {
                adapter.setBannerList(it)
                initIndicator(it)
                //设置默认值为第“1”页，而不是默认的第“0”页
                binding.bannerViewPager.setCurrentItem(1,false)
            }
        }

        private fun initIndicator(list: List<ApiBanner>){
            binding.indicatorLayout.removeAllViews()
            for (index in 1..list.size-2) {
                val imageView = ImageView(binding.root.context)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.marginEnd = 4
                layoutParams.marginStart = 4
                imageView.layoutParams = layoutParams
                binding.indicatorLayout.addView(imageView)
            }
        }

        private fun updateIndicator(position: Int){
            binding.indicatorLayout.run {
                for (index in 1..childCount) {
                    getChildAt(index - 1).setBackgroundResource(R.drawable.indicator_normal)
                }
                if (position > 0) {
                    getChildAt(position - 1).setBackgroundResource(R.drawable.indicator_selected)
                }
            }
        }
    }
}

