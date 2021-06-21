package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.MutableLiveData
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
 * .ProjectViewModel
 *
 * @author Admitor
 * @date 2021/06/21
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {

    /**
     * 搜索内容
     */
    val keyword: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private var currentSearchResult: Flow<PagingData<ApiArticle>>? = null

    fun getSearchArticle(): Flow<PagingData<ApiArticle>> {
        val newResult: Flow<PagingData<ApiArticle>> =
            repository.getSearchArticle(keyword.value ?: "").cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun collect(id: Int) = liveData {
        emit(repository.collect(id))
    }

    fun unCollect(id: Int) = liveData {
        emit(repository.unCollect(id))
    }
}