package com.longjunhao.wanjetpack.viewmodels

//import com.longjunhao.wanjetpack.util.SharedPreferencesObject
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.data.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
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
     * 用户信息
     */
    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    /**
     * 显示的用户名
     */
    val name = user.map {
        if (TextUtils.isEmpty(it.nickname)) it.username else it.nickname
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
        emit(repository.login(username.value ?: "", password.value ?: ""))
    }

    fun register() = liveData {
        emit(
            repository.register(
                username.value ?: "",
                password.value ?: "",
                repassword.value ?: ""
            )
        )
    }

    fun logout() = liveData {
        emit(repository.logout())
    }
}