package com.longjunhao.wanjetpack.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.longjunhao.wanjetpack.adapter.HomeArticleAdapter
import com.longjunhao.wanjetpack.databinding.FragmentHomeArticleBinding
import com.longjunhao.wanjetpack.viewmodels.HomeArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeArticleFragment : Fragment() {

    private var homeJob: Job? = null
    private val viewModel: HomeArticleViewModel by viewModels()
    private lateinit var binding: FragmentHomeArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeArticleBinding.inflate(inflater,container,false)
        context ?: return binding.root

        val adapter = HomeArticleAdapter(viewModel, viewLifecycleOwner)
        binding.articleList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: HomeArticleAdapter) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            viewModel.getHomeArticle().collectLatest {
                adapter.submitData(it)
            }
        }

        viewModel.bannerList.observe(viewLifecycleOwner, Observer {
            Log.d("HomeArticleFragment", "subscribeUi: ljh 11  it.code="+it.errorCode)
            if (it.errorCode == 0) {
                it.data?.let { list ->
                    val imageList = list.map { banner -> banner.imagePath }
                    Log.d("HomeArticleFragment", "subscribeUi: ljh 22 imageList="+imageList[0])
                    //firstAdapter.updateBannerList(list)
                }
            }
        })
    }
}