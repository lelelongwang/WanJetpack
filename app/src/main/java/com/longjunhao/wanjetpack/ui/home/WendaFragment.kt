package com.longjunhao.wanjetpack.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.longjunhao.wanjetpack.adapter.HomeArticleAdapter
import com.longjunhao.wanjetpack.adapter.WendaAdapter
import com.longjunhao.wanjetpack.databinding.FragmentWendaBinding
import com.longjunhao.wanjetpack.viewmodels.WendaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WendaFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: WendaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWendaBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val adapter = WendaAdapter()
        binding.articleList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: WendaAdapter) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            viewModel.getWenda().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}