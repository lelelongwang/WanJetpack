package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.ApiResponse
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * .WendaViewModel
 *
 * @author Admitor
 * @date 2021/06/11
 */
@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {
    private var currentCollectionArticleResult: Flow<PagingData<ApiArticle>>? = null

    fun getCollectionArticle(): Flow<PagingData<ApiArticle>> {
        val newResult: Flow<PagingData<ApiArticle>> =
            repository.getCollectionArticle().cachedIn(viewModelScope)
        currentCollectionArticleResult = newResult
        return newResult
    }

    fun unCollect(id: Int, originId: Int) = liveData {
        try {
            emit(repository.unCollect(id, originId))
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }
}