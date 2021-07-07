package com.longjunhao.wanjetpack.ui.home

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
import com.longjunhao.wanjetpack.adapter.WendaAdapter
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.FragmentWendaBinding
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import com.longjunhao.wanjetpack.viewmodels.WendaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WendaFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: WendaViewModel by viewModels()
    private lateinit var binding: FragmentWendaBinding
    private lateinit var articleAdapter: WendaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWendaBinding.inflate(inflater,container,false)
        context ?: return binding.root

        articleAdapter = WendaAdapter { apiArticle, position -> adapterFavoriteOnClick(apiArticle, position) }
        binding.articleList.adapter = articleAdapter.withLoadStateFooter(
            FooterAdapter(articleAdapter::retry)
        )
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            articleAdapter.refresh()
        }
        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            viewModel.getWenda().collectLatest {
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
                } else if (it.errorCode == API_RESPONSE_NO_NET) {
                    Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
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
                } else if (it.errorCode == API_RESPONSE_NO_NET) {
                    Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }
}