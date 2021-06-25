package com.longjunhao.wanjetpack.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.longjunhao.wanjetpack.adapter.HomeArticleAdapter
import com.longjunhao.wanjetpack.adapter.HomeFirstAdapter
import com.longjunhao.wanjetpack.data.home.ApiBanner
import com.longjunhao.wanjetpack.databinding.FragmentHomeArticleBinding
import com.longjunhao.wanjetpack.viewmodels.HomeArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeArticleFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: HomeArticleViewModel by viewModels()
    private lateinit var binding: FragmentHomeArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeArticleBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val firstAdapter = HomeFirstAdapter()
        val adapter = HomeArticleAdapter(viewModel, viewLifecycleOwner)
        val concatAdapter = ConcatAdapter(firstAdapter, adapter)

        binding.articleList.adapter = concatAdapter
        subscribeUi(adapter, firstAdapter)

        return binding.root
    }

    private fun subscribeUi(adapter: HomeArticleAdapter, firstAdapter: HomeFirstAdapter) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            viewModel.getHomeArticle().collectLatest {
                adapter.submitData(it)
            }
        }

        viewModel.bannerList.observe(viewLifecycleOwner, Observer {
            Log.d("HomeArticleFragment", "bannerList: ljh  it.code="+it.errorCode)
            if (it.errorCode == 0) {
                it.data?.let { list ->
                    val newList = arrayListOf<ApiBanner>()
                    newList.add(list[list.size-1])
                    for (item in list) {
                        newList.add(item)
                    }
                    newList.add(list[0])
                    firstAdapter.setBannerList(newList)

                    //下面的是调试log
                    val originalBannerList = list.map { banner -> banner.id }
                    val newBannerList = newList.map { banner -> banner.id }
                    Log.d("HomeArticleFragment", "bannerList: ljh originalBannerList=$originalBannerList  newBannerList=$newBannerList")
                }
            }
        })

        viewModel.autoScrollBannerPage.observe(viewLifecycleOwner, Observer {
            //todo 是否可以把it去掉？
            firstAdapter.autoScrollBannerPage()
            Log.d("HomeArticleFragment", "autoScrollBannerPage: ljh =$it")
        })
    }
}