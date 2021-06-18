package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.WanJetpackRepository
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
    private var currentWechatResult: Flow<PagingData<ApiArticle>>? = null

    fun getWechatArticle(wechatId: Int): Flow<PagingData<ApiArticle>> {
        currentIdValue = wechatId
        val newResult: Flow<PagingData<ApiArticle>> =
            repository.getWechatArticle(wechatId).cachedIn(viewModelScope)
        currentWechatResult = newResult
        return newResult
    }

    fun collect(id: Int) = liveData {
        emit(repository.collect(id))
    }

    fun unCollect(id: Int) = liveData {
        emit(repository.unCollect(id))
    }
}