package com.longjunhao.wanjetpack.ui.wechat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.data.wechat.WechatCategory
import com.longjunhao.wanjetpack.databinding.FragmentWechatBinding
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import com.longjunhao.wanjetpack.viewmodels.WechatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WechatFragment : Fragment() {

    private val viewModel: WechatViewModel by viewModels()
    private lateinit var binding: FragmentWechatBinding

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
            if (it.errorCode == 0) {
                it.data?.let { list -> initViewPager(list) }
            } else if (it.errorCode == API_RESPONSE_NO_NET) {
                Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun initViewPager(chapters: List<WechatCategory>){
        if (chapters.isEmpty()) return
        val titles = arrayOfNulls<String>(chapters.size)
        val wechatId = ArrayList<Int>()//解决crash：从本界面跳转到登录界面，返回到本界面时，wechatId.size和titles.size不一致的bug
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
            tab.setIcon(R.drawable.ic_tab_selector)
        }.attach()
    }
}
