package com.longjunhao.wanjetpack.ui.project

import android.os.Bundle
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
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
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
    private lateinit var categoryAdapter: ProjectCategoryAdapter
    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        categoryAdapter = ProjectCategoryAdapter { category -> adapterCategoryOnClick(category) }
        projectAdapter = ProjectAdapter { apiArticle, position -> adapterFavoriteOnClick(apiArticle, position) }
        binding.projectCategoryList.adapter = categoryAdapter
        binding.projectList.adapter = projectAdapter.withLoadStateFooter(
            FooterAdapter(projectAdapter::retry)
        )
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi(){
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.jetpack_green_900)
            setProgressBackgroundColorSchemeResource(R.color.jetpack_green_500)
            setOnRefreshListener {
                projectAdapter.refresh()
            }
        }
        lifecycleScope.launchWhenCreated {
            projectAdapter.loadStateFlow.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        viewModel.projectCategory.observe(viewLifecycleOwner, Observer { response ->
            if (response.errorCode == 0) {
                categoryAdapter.submitList(response.data)
                //默认加载第一个项目分类
                response.data?.let {
                    adapterCategoryOnClick(it[0])
                }
            } else if (response.errorCode == API_RESPONSE_NO_NET) {
                Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
            }
        })
    }

    /**
     * todo 遗留问题：每次点击时，project_list并没有滚动到最顶部
     */
    private fun adapterCategoryOnClick(projectCategory: ProjectCategory) {
        categoryAdapter.currentList.forEach {
            it.isSelected = false
        }
        projectCategory.isSelected = true
        binding.toolbarText.text = projectCategory.name
        categoryAdapter.notifyDataSetChanged()

        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            viewModel.getProjectArticle(projectCategory.id).collectLatest {
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
                } else if (it.errorCode == API_RESPONSE_NO_NET) {
                    Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
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
                } else if (it.errorCode == API_RESPONSE_NO_NET) {
                    Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }

}