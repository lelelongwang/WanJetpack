package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.data.project.ProjectArticle
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
    private var currentSearchResult: Flow<PagingData<ProjectArticle>>? = null

    val currentSelectedItem: MutableLiveData<ProjectCategory> by lazy {
        MutableLiveData<ProjectCategory>()
    }

    val projectCategory = repository.getProjectCategory()

    fun getProjectArticle(categoryId: Int): Flow<PagingData<ProjectArticle>> {
        currentQueryValue = categoryId
        val newResult: Flow<PagingData<ProjectArticle>> =
            repository.getProjectArticle(categoryId).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}