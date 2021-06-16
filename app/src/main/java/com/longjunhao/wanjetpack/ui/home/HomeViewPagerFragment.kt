package com.longjunhao.wanjetpack.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.HOME_ARTICLE_INDEX
import com.longjunhao.wanjetpack.adapter.HomeViewPagerAdapter
import com.longjunhao.wanjetpack.adapter.VIEW_PAGER_INDEX_TWO
import com.longjunhao.wanjetpack.databinding.FragmentHomeViewPagerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IndexOutOfBoundsException

@AndroidEntryPoint
class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeViewPagerBinding.inflate(inflater,container,false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = HomeViewPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabIcon(position: Int): Int{
        return when (position) {
            HOME_ARTICLE_INDEX -> R.drawable.ic_launcher_foreground
            VIEW_PAGER_INDEX_TWO -> R.drawable.ic_launcher_foreground
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            HOME_ARTICLE_INDEX -> getString(R.string.home_article)
            VIEW_PAGER_INDEX_TWO -> getString(R.string.home_wenda)
            else -> null
        }
    }
}