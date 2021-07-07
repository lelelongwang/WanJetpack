package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.longjunhao.wanjetpack.data.ApiResponse
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
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
        try {
            emit( repository.getWechatName())
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }
}