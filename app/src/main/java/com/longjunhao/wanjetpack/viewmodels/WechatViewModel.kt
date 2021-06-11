package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.*
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * .WechatViewModel
 *
 * @author Admitor
 * @date 2021/05/26
 */
@HiltViewModel
class WechatViewModel @Inject constructor(
    private val repository: WanJetpackRepository
) : ViewModel() {

    /**
     * TODO: 1. 没有用LiveData，不知道需不需要, 2. 为什么没有用Transformations.map()和Transformations.switchMap(),3. LiveData用Flow替换了
     *
     */
    val wechatName = repository.getWechatName().map {
        it.data ?: ArrayList()
    }
}