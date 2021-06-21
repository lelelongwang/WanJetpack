package com.longjunhao.wanjetpack.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.longjunhao.wanjetpack.adapter.SearchAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater,container,false)
        context ?: return binding.root

        //databinding 双向绑定时要加上这行，否则获取 keyword 值为 null
        binding.searchModel = viewModel

        val adapter = SearchAdapter(viewModel, viewLifecycleOwner)
        binding.articleList.adapter = adapter

        binding.searchCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                subscribeUi(adapter)
            }
            true
        }

        // todo：
        //  当没有登录账号时，从搜索界面进入登录界面，再返回到搜索界面，没有搜索结果了，需要重新网络请求或者有缓存显示也行。
        //  但是如果按照下面的方案，通过监听viewModel.keyword来解决的话，这样在搜索框中输入文本的过程中也会实时网络请
        //  求，会频繁的网络请求，感觉也不太好。故暂时注掉代码
        /*viewModel.keyword.observe(viewLifecycleOwner, Observer {
            subscribeUi(adapter)
        })*/

        return binding.root
    }

    private fun subscribeUi(adapter: SearchAdapter){
        homeJob?.cancel()
        homeJob = lifecycleScope.launch {
            viewModel.getSearchArticle().collectLatest {
                adapter.submitData(it)
            }
        }
    }

}