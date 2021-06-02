package com.longjunhao.wanjetpack.ui.wechat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.longjunhao.wanjetpack.adapter.WechatAdapter
import com.longjunhao.wanjetpack.databinding.FragmentWechatArticleBinding
import com.longjunhao.wanjetpack.viewmodels.WechatArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val WECHAT_TAB_ID = "id"

@AndroidEntryPoint
class WechatArticleFragment : Fragment() {

    private var wechatId: Int? = null
    private var homeJob: Job? = null
    private val viewModel: WechatArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wechatId = it.getInt(WECHAT_TAB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWechatArticleBinding.inflate(inflater,container,false)
        val adapter = WechatAdapter()
        binding.articleList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: WechatAdapter) {
        homeJob?.cancel()
        homeJob = lifecycleScope.launch(){
            wechatId?.let { wechatId ->
                viewModel.getWechatArticle(wechatId).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        fun create(id: Int) =
            WechatArticleFragment().apply {
                arguments = Bundle(1).apply {
                    putInt(WECHAT_TAB_ID, id)
                }
            }
    }

}