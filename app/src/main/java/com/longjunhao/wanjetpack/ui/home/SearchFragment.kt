package com.longjunhao.wanjetpack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.FooterAdapter
import com.longjunhao.wanjetpack.adapter.SearchAdapter
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.FragmentSearchBinding
import com.longjunhao.wanjetpack.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var articleAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        context ?: return binding.root

        //databinding 双向绑定时要加上这行，否则获取 keyword 值为 null
        binding.searchModel = viewModel

        articleAdapter = SearchAdapter { apiArticle, position -> adapterFavoriteOnClick(apiArticle, position) }
        binding.articleList.adapter = articleAdapter.withLoadStateFooter(
            FooterAdapter(articleAdapter::retry)
        )
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            articleAdapter.refresh()
        }
        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        binding.searchCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                articleSearch()
            }
            true
        }

        // todo：
        //  当没有登录账号时，从搜索界面进入登录界面，再返回到搜索界面，没有搜索结果了，需要重新网络请求或者有缓存显示也行。
        //  但是如果按照下面的方案，通过监听viewModel.keyword来解决的话，这样在搜索框中输入文本的过程中也会实时网络请
        //  求，会频繁的网络请求，感觉也不太好。故暂时注掉代码
        /*viewModel.keyword.observe(viewLifecycleOwner, Observer {
            articleSearch()
        })*/
    }

    private fun articleSearch() {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            viewModel.getSearchArticle().collectLatest {
                articleAdapter.submitData(it)
            }
        }
    }

    private fun adapterFavoriteOnClick (article: ApiArticle, position: Int) {
        if (article.collect) {
            viewModel.unCollect(article.id).observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    article.collect = false
                    articleAdapter.notifyItemChanged(position)
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
                    articleAdapter.notifyItemChanged(position)
                    Snackbar.make(binding.root, "收藏成功", Snackbar.LENGTH_LONG).show()
                } else if (it.errorCode == -1001) {
                    findNavController().navigate(R.id.loginFragment)
                }
            })
        }
    }

}