package com.longjunhao.wanjetpack.ui.wechat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.FooterAdapter
import com.longjunhao.wanjetpack.adapter.WechatAdapter
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.FragmentWechatArticleBinding
import com.longjunhao.wanjetpack.viewmodels.WechatArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val WECHAT_TAB_ID = "id"

@AndroidEntryPoint
class WechatArticleFragment : Fragment() {

    private var wechatId: Int? = null
    private var homeJob: Job? = null
    private val viewModel: WechatArticleViewModel by viewModels()
    private lateinit var binding: FragmentWechatArticleBinding
    private lateinit var adapter: WechatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wechatId = it.getInt(WECHAT_TAB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWechatArticleBinding.inflate(inflater,container,false)
        adapter = WechatAdapter { apiArticle, position -> adapterFavoriteOnClick(apiArticle, position) }
        binding.articleList.adapter = adapter.withLoadStateFooter(
            FooterAdapter(adapter::retry)
        )
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            wechatId?.let { wechatId ->
                viewModel.getWechatArticle(wechatId).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        fun create(id: Int) =
            WechatArticleFragment().apply {
                arguments = Bundle(1).apply {
                    putInt(WECHAT_TAB_ID, id)
                }
            }
    }

    private fun adapterFavoriteOnClick (article: ApiArticle, position: Int) {
        if (article.collect) {
            viewModel.unCollect(article.id).observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    article.collect = false
                    adapter.notifyItemChanged(position)
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
                    adapter.notifyItemChanged(position)
                    Snackbar.make(binding.root, "收藏成功", Snackbar.LENGTH_LONG).show()
                } else if (it.errorCode == -1001) {
                    findNavController().navigate(R.id.loginFragment)
                }
            })
        }
    }

}