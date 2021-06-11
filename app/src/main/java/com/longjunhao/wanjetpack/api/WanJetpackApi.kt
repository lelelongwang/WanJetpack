package com.longjunhao.wanjetpack.api

import android.util.Log
import androidx.lifecycle.LiveData
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.ApiPage
import com.longjunhao.wanjetpack.data.HomeArticle
import com.longjunhao.wanjetpack.data.Wenda
import com.longjunhao.wanjetpack.data.project.ProjectArticle
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import com.longjunhao.wanjetpack.data.user.User
import com.longjunhao.wanjetpack.data.wechat.Wechat
import com.longjunhao.wanjetpack.data.wechat.WechatArticle
import com.longjunhao.wanjetpack.util.SharedPrefObject
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.math.log

/**
 * .WanJetpackApi
 *
 * @author Admitor
 * @date 2021/05/24
 */
interface WanJetpackApi {

    /**
     * 获取首页文章列表
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomeArticle(
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<HomeArticle>>

    /**
     * 获取问答列表
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getWenda(
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<Wenda>>

    /**
     * 获取项目分类列表
     */
    @GET("/project/tree/json")
    fun getProjectCategory(): LiveData<ApiResponse<List<ProjectCategory>>>

    /**
     * 获取项目列表数据
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectArticle(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): WanJetResponse<ApiPage<ProjectArticle>>

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/chapters/json")
    fun getWechatName(): LiveData<ApiResponse<List<Wechat>>>

    /**
     * 获取某个公众号历史数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticle(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<WechatArticle>>

    /**
     * 登录
     */
    @POST("/user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LiveData<ApiResponse<User>>
//    ): LiveData<ApiResponse<User>>

    /**
     * 注册成功和登录成功的json一样，通过判断errorCode即可。
     */
    @POST("/user/register")
    fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): LiveData<ApiResponse<User>>

    /**
     * 退出,todo 清理cookie
     * 退出成功为是判断errorCode即可：{"data":null,"errorCode":0,"errorMsg":""}
     */
    @GET("/user/logout/json")
    fun logout(): LiveData<ApiResponse<User>>

    /**
     * 获取收藏列表，请求后的json和 获取首页文章列表一样。
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollection(
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<ApiArticle>>


    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        fun create(): WanJetpackApi {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .cookieJar(LocalCookie())
//                .cookieJar(LocalCookieJar())
                .build()

            //注意：如果只是用Paging、Flow不需要LiveDataCallAdapterFactory或CoroutineCallAdapterFactory。
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(WanJetpackApi::class.java)
        }
    }
}

/**
 * Cookie 本地化方案一
 */
class LocalCookie : CookieJar {
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = SharedPrefObject.getCookies()
        Log.d("LocalCookie", "loadForRequest: ljh cookies=$cookies")
        return cookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        Log.d("LocalCookie", "saveFromResponse: ljh cookies=$cookies")
        if (url.toString().startsWith("https://www.wanandroid.com/user/login?")) {
            SharedPrefObject.saveCookies(cookies)
        }
    }
}

/**
 * Cookie 本地化方案二
 */
class LocalCookieJar : CookieJar {
    //cookie的本地化存储
    private val cache = mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        //过期的Cookie
        val invalidCookies: MutableList<Cookie> = ArrayList()
        //有效的Cookie
        val validCookies: MutableList<Cookie> = ArrayList()

        for (cookie in cache) {
            if (cookie.expiresAt < System.currentTimeMillis()) {
                //判断是否过期
                invalidCookies.add(cookie)
            } else if (cookie.matches(url)) {
                //匹配Cookie对应url
                validCookies.add(cookie)
            }
        }
        //缓存中移除过期的Cookie
        cache.removeAll(invalidCookies)

        //返回List<Cookie>让Request进行设置
        return validCookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cache.addAll(cookies)
    }

}