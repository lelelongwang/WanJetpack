package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.CollectionArticleAdapter
import com.longjunhao.wanjetpack.adapter.FooterAdapter
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.databinding.FragmentCollectionBinding
import com.longjunhao.wanjetpack.util.API_RESPONSE_LOGIN_INVALID
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import com.longjunhao.wanjetpack.util.API_RESPONSE_SUCCESS
import com.longjunhao.wanjetpack.viewmodels.CollectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: CollectionViewModel by viewModels()
    private lateinit var binding: FragmentCollectionBinding
    private lateinit var adapter: CollectionArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollectionBinding.inflate(inflater, container, false)
        context ?: return binding.root

        adapter = CollectionArticleAdapter()
        binding.articleList.adapter = adapter.withLoadStateFooter(
            FooterAdapter(adapter::retry)
        )
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.jetpack_green_900)
            setProgressBackgroundColorSchemeResource(R.color.jetpack_green_500)
            setOnRefreshListener {
                adapter.refresh()
            }
        }
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            viewModel.getCollectionArticle().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    /**
     * todo 期望调用notifyItemRemoved即可，但是如果只调用notifyItemRemoved的话，再刷新显示会有异常。
     *   暂时规避方案为：不用notifyItemRemoved，直接调用adapter.refresh()删除成功后刷新界面（再次网络请求）
     */
    private fun unCollect(article: ApiArticle, position: Int) {
        viewModel.unCollect(article.id, article.originId).observe(viewLifecycleOwner, {
            val snackMessage = when (it.errorCode) {
                API_RESPONSE_SUCCESS -> {
                    adapter.notifyItemRemoved(position)
                    //adapter.refresh()
                    getString(R.string.un_collect_success)
                }
                API_RESPONSE_LOGIN_INVALID -> getString(R.string.login_invalid)
                API_RESPONSE_NO_NET -> getString(R.string.no_net)
                else -> getString(R.string.unknown_bug)
            }
            Snackbar.make(binding.root, snackMessage, Snackbar.LENGTH_LONG).show()
        })
    }
}