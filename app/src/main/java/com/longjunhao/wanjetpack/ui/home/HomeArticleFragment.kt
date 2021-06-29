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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.HomeArticleAdapter
import com.longjunhao.wanjetpack.adapter.HomeFirstAdapter
import com.longjunhao.wanjetpack.data.ApiArticle
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
    private lateinit var articleAdapter: HomeArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeArticleBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val firstAdapter = HomeFirstAdapter()
        articleAdapter = HomeArticleAdapter { apiArticle -> adapterFavoriteOnClick(apiArticle) }
        val concatAdapter = ConcatAdapter(firstAdapter, articleAdapter)

        binding.articleList.adapter = concatAdapter
        subscribeUi(firstAdapter)

        return binding.root
    }

    private fun subscribeUi(firstAdapter: HomeFirstAdapter) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            viewModel.getHomeArticle().collectLatest {
                articleAdapter.submitData(it)
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

    /**
     * 通过官方推荐的方案实现
     * 参考：https://mp.weixin.qq.com/s/xtYtmn2zPRFcM4-uTVW3HA
     */
    private fun adapterFavoriteOnClick (article: ApiArticle) {
        if (article.collect) {
            viewModel.unCollect(article.id).observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    article.collect = false
                    articleAdapter.notifyDataSetChanged()
                    Snackbar.make(binding.root, "取消收藏成功", Snackbar.LENGTH_LONG).show()
                } else if (it.errorCode == -1001) {
                    //没有登录的话，collect为false，故下面的代码应该不会执行。
                    Snackbar.make(binding.root, "未知的场景，请提bug", Snackbar.LENGTH_LONG).show()
                }
            })
        } else {
            viewModel.collect(article.id).observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    article.collect = true
                    articleAdapter.notifyDataSetChanged()
                    Snackbar.make(binding.root, "收藏成功", Snackbar.LENGTH_LONG).show()
                } else if (it.errorCode == -1001) {
                    //todo 这是通过网络返回发现没有登录，是否需要多加个条件：新增Boolean类型的isLogin保存
                    // 在SharedPreferences中呢? 登录动画需要优化
                    findNavController().navigate(R.id.loginFragment)
                }
            })
        }
    }
}