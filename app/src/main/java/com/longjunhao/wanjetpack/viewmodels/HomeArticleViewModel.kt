package com.longjunhao.wanjetpack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.ApiResponse
import com.longjunhao.wanjetpack.data.WanJetpackRepository
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.net.UnknownHostException
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
    private var currentArticleResult: Flow<PagingData<ApiArticle>>? = null

    fun getHomeArticle(): Flow<PagingData<ApiArticle>> {
        val newResult: Flow<PagingData<ApiArticle>> =
            repository.getHomeArticle().cachedIn(viewModelScope)
        currentArticleResult = newResult
        return newResult
    }

    /**
     *
     * 1. 当 LiveData 被观察的时候，里面的操作就会被执行，当 LiveData 不再被使用时，里面的操作就会取消。
     *
     * 2. liveData 协程构造方法提供了一个协程代码块，这个块就是 LiveData 的作用域，当 LiveData 被观察的时候，
     *  里面的操作就会被执行，当 LiveData 不再被使用时，里面的操作就会取消。而且该协程构造方法产生的是一个不可变
     *  的 LiveData，可以直接暴露给对应的视图使用。而 emit() 方法则用来更新 LiveData 的数据。
     *
     *  参考： https://mp.weixin.qq.com/s/p5H51RC6QfyyoAcQ1aGRLg
     *
     *
     * // A retrying data fetcher with doubling back-off
     * val user = liveData {
     *     var backOffTime = 1_000
     *     var succeeded = false
     *     while(!succeeded) {
     *         try {
     *             emit(api.fetch(id))
     *             succeeded = true
     *         } catch(ioError : IOException) {
     *             delay(backOffTime)
     *             backOffTime *= minOf(backOffTime * 2, 60_000)
     *         }
     *     }
     * }
     *
     * todo :
     *  banner、项目分类（projectCategory）、微信公众号名（wechatName）需要用上面的方案来解决网络重新打开
     *  后，自动网络请求获取数据
     *
     */
    val bannerList = liveData {
        try {
            emit(repository.getBanner())
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }

    /**
     * 通过autoScrollBannerPage.observe()触发没三秒滚动一下banner
     */
    val autoScrollBannerPage = liveData {
        var counter = 0
        while (true) {
            delay(3000)
            counter++
            emit(counter)
        }
    }

    /**
     * todo ：和adapter一样，重复的部分应该可以写在baseViewModel中
     */
    fun collect(id: Int) = liveData {
        try {
            emit(repository.collect(id))
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }

    /**
     * todo ：和adapter一样，重复的部分应该可以写在baseViewModel中
     */
    fun unCollect(id: Int) = liveData {
        try {
            emit(repository.unCollect(id))
        } catch (e: UnknownHostException) {
            emit(ApiResponse(null, API_RESPONSE_NO_NET, e.toString()))
        }
    }
}