package com.longjunhao.wanjetpack.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.longjunhao.wanjetpack.ui.home.HomeArticleFragment
import com.longjunhao.wanjetpack.ui.home.WendaFragment

/**
 * .WanJetpackPagerAdapter
 *
 * @author Admitor
 * @date 2021/05/21
 */

const val HOME_ARTICLE_INDEX = 0
const val VIEW_PAGER_INDEX_TWO = 1

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HOME_ARTICLE_INDEX to { HomeArticleFragment() },
        VIEW_PAGER_INDEX_TWO to { WendaFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}