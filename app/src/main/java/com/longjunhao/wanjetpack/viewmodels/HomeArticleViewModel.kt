package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.HomeArticle
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * .HomeArticleViewMode
 *
 * @author Admitor
 * @date 2021/05/24
 */
@HiltViewModel
class HomeArticleViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {
    private var currentArticleResult: Flow<PagingData<HomeArticle>>? = null

    fun getHomeArticle(): Flow<PagingData<HomeArticle>> {
        val newResult: Flow<PagingData<HomeArticle>> =
            repository.getHomeArticle().cachedIn(viewModelScope)
        currentArticleResult = newResult
        return newResult
    }
}