package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * .ProjectViewModel
 *
 * @author Admitor
 * @date 2021/05/28
 */
@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {

    private var currentQueryValue: Int? = null
    private var currentSearchResult: Flow<PagingData<ApiArticle>>? = null

    val currentSelectedItem: MutableLiveData<ProjectCategory> by lazy {
        MutableLiveData<ProjectCategory>()
    }

    val projectCategory = liveData {
        emit(repository.getProjectCategory())
    }

    fun getProjectArticle(categoryId: Int): Flow<PagingData<ApiArticle>> {
        currentQueryValue = categoryId
        val newResult: Flow<PagingData<ApiArticle>> =
            repository.getProjectArticle(categoryId).cachedIn(viewModelScope)
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