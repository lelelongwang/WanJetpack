package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
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
}