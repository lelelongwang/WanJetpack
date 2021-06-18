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
import com.longjunhao.wanjetpack.adapter.ProjectAdapter
import com.longjunhao.wanjetpack.adapter.ProjectCategoryAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)

        val categoryAdapter = ProjectCategoryAdapter(viewModel)
        val projectAdapter = ProjectAdapter(viewModel, viewLifecycleOwner)
        binding.projectCategoryList.adapter = categoryAdapter
        binding.projectList.adapter = projectAdapter
        subscribeUi(categoryAdapter, projectAdapter)

        return binding.root
    }

    private fun subscribeUi(categoryAdapter: ProjectCategoryAdapter,adapter: ProjectAdapter){
        viewModel.projectCategory.observe(viewLifecycleOwner, Observer {
            if (it.errorCode == 0) {
                categoryAdapter.submitList(it.data)
                viewModel.currentSelectedItem.postValue(it.data?.get(0))
            }
        })
        viewModel.currentSelectedItem.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "subscribeUi: ljh it${it.id}+ name${it.name}")
            updateSelectItem(adapter,it)
        })
    }

    private fun updateSelectItem(adapter: ProjectAdapter, projectCategory: ProjectCategory) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            //TODO toolbarText的值尽量在xml中设置
            binding.toolbarText.text = projectCategory.name
            viewModel.getProjectArticle(projectCategory.id).collectLatest {
                Log.d("TAG", "updateSelectItem: 测试。 it$it")
                adapter.submitData(it)
            }
        }
    }

}