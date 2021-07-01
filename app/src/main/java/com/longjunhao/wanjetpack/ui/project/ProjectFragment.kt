package com.longjunhao.wanjetpack.ui.project

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
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.adapter.FooterAdapter
import com.longjunhao.wanjetpack.adapter.ProjectAdapter
import com.longjunhao.wanjetpack.adapter.ProjectCategoryAdapter
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import com.longjunhao.wanjetpack.databinding.FragmentProjectBinding
import com.longjunhao.wanjetpack.viewmodels.ProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: ProjectViewModel by viewModels()
    private lateinit var binding: FragmentProjectBinding
    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)

        val categoryAdapter = ProjectCategoryAdapter(viewModel)
        projectAdapter = ProjectAdapter { apiArticle, position -> adapterFavoriteOnClick(apiArticle, position) }
        binding.projectCategoryList.adapter = categoryAdapter
        binding.projectList.adapter = projectAdapter.withLoadStateFooter(
            FooterAdapter(projectAdapter::retry)
        )
        subscribeUi(categoryAdapter)

        return binding.root
    }

    private fun subscribeUi(categoryAdapter: ProjectCategoryAdapter){
        binding.swipeRefreshLayout.setOnRefreshListener {
            projectAdapter.refresh()
        }
        lifecycleScope.launchWhenCreated {
            projectAdapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        viewModel.projectCategory.observe(viewLifecycleOwner, Observer {
            if (it.errorCode == 0) {
                categoryAdapter.submitList(it.data)
                viewModel.currentSelectedItem.postValue(it.data?.get(0))
            }
        })
        viewModel.currentSelectedItem.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "subscribeUi: ljh it${it.id}+ name${it.name}")
            updateSelectItem(it)
        })
    }

    private fun updateSelectItem(projectCategory: ProjectCategory) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            //TODO toolbarText的值尽量在xml中设置
            binding.toolbarText.text = projectCategory.name
            viewModel.getProjectArticle(projectCategory.id).collectLatest {
                Log.d("TAG", "updateSelectItem: 测试。 it$it")
                projectAdapter.submitData(it)
            }
        }
    }

    private fun adapterFavoriteOnClick (article: ApiArticle, position: Int) {
        if (article.collect) {
            viewModel.unCollect(article.id).observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    article.collect = false
                    projectAdapter.notifyItemChanged(position)
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
                    projectAdapter.notifyItemChanged(position)
                    Snackbar.make(binding.root, "收藏成功", Snackbar.LENGTH_LONG).show()
                } else if (it.errorCode == -1001) {
                    findNavController().navigate(R.id.loginFragment)
                }
            })
        }
    }

}