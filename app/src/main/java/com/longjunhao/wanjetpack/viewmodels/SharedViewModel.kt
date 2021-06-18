package com.longjunhao.wanjetpack.viewmodels

//import com.longjunhao.wanjetpack.util.SharedPreferencesObject
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
     * 用户名
     */
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * todo 为什么不行获取不到username、password的值呢？
     */
    /*val login = repository.login(username.value ?: "", password.value ?: "").map {
        if (it.errorCode == 0) it.data else null
    }*/

    fun login(username: String, password: String) = liveData {
        emit(repository.login(username, password))
    }

    fun register(username: String, password: String, repassword: String) = liveData {
        emit(repository.register(username, password, repassword))
    }

    val logout = liveData {
        emit(repository.logout())
    }
}