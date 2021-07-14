package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.CollectionArticleAdapter
import com.longjunhao.wanjetpack.adapter.FooterAdapter
import com.longjunhao.wanjetpack.databinding.FragmentCollectionBinding
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
        binding = FragmentCollectionBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val adapter = CollectionArticleAdapter()
        binding.articleList.adapter = adapter.withLoadStateFooter(
            FooterAdapter(adapter::retry)
        )
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: CollectionArticleAdapter){

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
}