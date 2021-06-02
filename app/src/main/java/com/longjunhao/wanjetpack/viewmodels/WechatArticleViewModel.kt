package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.data.Wenda
import com.longjunhao.wanjetpack.data.wechat.WechatArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * .WechatArticleViewModel
 *
 * @author Admitor
 * @date 2021/05/28
 */
@HiltViewModel
class WechatArticleViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {
    private var currentIdValue: Int? = null
    private var currentWechatResult: Flow<PagingData<WechatArticle>>? = null

    fun getWechatArticle(wechatId: Int): Flow<PagingData<WechatArticle>> {
        currentIdValue = wechatId
        val newResult: Flow<PagingData<WechatArticle>> =
            repository.getWechatArticle(wechatId).cachedIn(viewModelScope)
        currentWechatResult = newResult
        return newResult
    }
}