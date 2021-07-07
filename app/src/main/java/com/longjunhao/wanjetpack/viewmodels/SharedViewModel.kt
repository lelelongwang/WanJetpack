package com.longjunhao.wanjetpack.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.longjunhao.wanjetpack.data.ApiResponse
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import com.longjunhao.wanjetpack.util.SharedPrefObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * .SharedViewModel
 *
 * @author Admitor
 * @date 2021/06/09
 */

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: WanJetpackRepository
): ViewModel() {

    /**
     * 登录账号
     */
    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * 登录密码
     */
    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * 确认登录密码
     */
    val repassword: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * 是否已经登录
     */
    val isLogin: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * 显示的用户名
     * 登录状态本地化方案：
     * 1.在登录、注册、退出成功后会修改isLogin的值，同时会保存 KEY_IS_LOGIN、KEY_LOGIN_NAME。
     * 2.当isLogin状态发生变化时，会从KEY_LOGIN_NAME中获取name的值。这样当登录状态发生变化时，name能够正确显示
     * 3.当kill进程后，首次进入UserFragment界面时，会通过调用init{}代码块，获取isLogin的状态。进而来更新name
     */
    val name = isLogin.map {
        Log.d("SharedViewModel", "ljh : name, isLogin="+isLogin.value)
        SharedPrefObject.getString(SharedPrefObject.KEY_LOGIN_NAME)
    }

    init {
        Log.d("SharedViewModel init", "ljh : isLogin="+isLogin.value+"  name="+name.value)
        if (isLogin.value == null) {
            isLogin.postValue(SharedPrefObject.getBoolean(SharedPrefObject.KEY_IS_LOGIN))
        }
    }

    /**
     * 用了databinding双向绑定了，是不用传参的，直接从xml里获取即可。
     *
     * 注意 fun login() 和 val login 和 var login 的区别：
     * 此处不能通过val login定义一个常量或者变量。要用fun login()方法，这样才能确保每次都有网络请求。
     * 如果改为val login的话，在登录之后->退出登录->再次登录时不会网络请求，直接显示登陆成功，且会显示退出登录
     * 之前的账号。因为在上面的退出登录后，val login并没有改变，所以再次登录时，由于是通过viewModel.login.observe
     * 的方式，值没有变，故因为当点击登录按钮时，不会网络请求。如果该为fun login()的话，每次点击按钮都会执行，
     * 然后会observe执行的结果。
     */
    fun login() = liveData {
        try {
            emit(repository.login(username.value ?: "", password.value ?: ""))
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }

    fun register() = liveData {
        try {
            emit(
                repository.register(
                    username.value ?: "",
                    password.value ?: "",
                    repassword.value ?: ""
                )
            )
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }

    fun logout() = liveData {
        try {
            emit(repository.logout())
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }
}