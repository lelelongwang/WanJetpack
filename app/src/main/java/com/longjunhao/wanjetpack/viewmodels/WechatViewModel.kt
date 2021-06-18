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

    val wechatName = liveData {
        emit( repository.getWechatName())
    }
}