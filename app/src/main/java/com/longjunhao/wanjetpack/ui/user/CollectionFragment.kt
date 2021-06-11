package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.longjunhao.wanjetpack.adapter.CollectionArticleAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCollectionBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val adapter = CollectionArticleAdapter()
        binding.articleList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: CollectionArticleAdapter){
        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            viewModel.getCollectionArticle().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}