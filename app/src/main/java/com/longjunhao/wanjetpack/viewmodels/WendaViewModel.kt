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
 * .WendaViewModel
 *
 * @author Admitor
 * @date 2021/05/25
 */
@HiltViewModel
class WendaViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {
    private var currentWendaResult: Flow<PagingData<ApiArticle>>? = null

    fun getWenda(): Flow<PagingData<ApiArticle>> {
        val newResult: Flow<PagingData<ApiArticle>> =
            repository.getWenda().cachedIn(viewModelScope)
        currentWendaResult = newResult
        return newResult
    }

    fun collect(id: Int) = liveData {
        emit(repository.collect(id))
    }

    fun unCollect(id: Int) = liveData {
        emit(repository.unCollect(id))
    }
}