package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.data.Wenda
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
    private var currentWendaResult: Flow<PagingData<Wenda>>? = null

    fun getWenda(): Flow<PagingData<Wenda>> {
        val newResult: Flow<PagingData<Wenda>> =
            repository.getWenda().cachedIn(viewModelScope)
        currentWendaResult = newResult
        return newResult
    }
}