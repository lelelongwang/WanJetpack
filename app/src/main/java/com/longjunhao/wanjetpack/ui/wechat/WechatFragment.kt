package com.longjunhao.wanjetpack.ui.wechat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.data.wechat.Wechat
import com.longjunhao.wanjetpack.databinding.FragmentWechatBinding
import com.longjunhao.wanjetpack.viewmodels.WechatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WechatFragment : Fragment() {

    private val viewModel: WechatViewModel by viewModels()
    private lateinit var binding: FragmentWechatBinding
    val wechatId = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWechatBinding.inflate(inflater,container,false)

        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        viewModel.wechatName.observe(viewLifecycleOwner, Observer {
            initViewPager(it)
        })
    }

    private fun initViewPager(chapters: List<Wechat>){
        if (chapters.isEmpty()) return
        val titles = arrayOfNulls<String>(chapters.size)
        chapters.forEachIndexed { index, wechat ->
            titles[index] = wechat.name
            wechatId.add(wechat.id)
        }
        val viewPager = binding.viewPager
        //如果WechatPagerAdapter是内部类的话，可能是由于hilt的原因，编译不过。要写成虚拟内部类或者外部类，但是如果是
        //外部类的话，wechatId不知道怎么传递到adapter。不知道用adapter的构造函数是否可行。
        //viewPager.adapter = WechatPagerAdapter(this)
        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = wechatId.size

            override fun createFragment(position: Int): Fragment {
                return WechatArticleFragment.create(wechatId[position])
            }
        }
        // Set the icon and text for each tab
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = titles[position]
            tab.setIcon(R.drawable.ic_launcher_foreground)
        }.attach()
    }
}
