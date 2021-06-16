package com.longjunhao.wanjetpack.api

import android.util.Log
import androidx.lifecycle.LiveData
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.ApiPage
import com.longjunhao.wanjetpack.data.ApiResponse
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import com.longjunhao.wanjetpack.data.user.User
import com.longjunhao.wanjetpack.data.wechat.WechatCategory
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
    ): WanJetResponse<ApiPage<ApiArticle>>

    /**
     * 获取问答列表
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getWenda(
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<ApiArticle>>

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
    ): WanJetResponse<ApiPage<ApiArticle>>

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/chapters/json")
    fun getWechatName(): LiveData<ApiResponse<List<WechatCategory>>>

    /**
     * 获取某个公众号历史数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticle(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<ApiArticle>>

    /**
     * 登录
     */
    @POST("/user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LiveData<ApiResponse<User>>

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
     * 获取收藏列表，请求后的json和 获取首页文章列表几乎一样。但是我的收藏获取的ApiArticle中没有"collect"字符串。
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollection(
        @Path("page") page: Int
    ): WanJetResponse<ApiPage<ApiArticle>>

    /**
     * 收藏站内文章：
     * 1. 如果id已经收藏，还可以继续收藏（可能收藏时间不一样，待确认）。如果是错误的id，也能收藏成功，但是在登录成功返
     * 回的json的collectIds是不存在错误的id的
     * 2. 收藏返回的json为{"data":null,"errorCode":0,"errorMsg":""}，故返回值中的ApiArticle是随便写的
     *
     * todo 还有一种常见没有实现：收藏站外文章
     */
    @POST("lg/collect/{id}/json")
    fun collect(
        @Path("id") id: Int
    ): LiveData<ApiResponse<ApiArticle>>

    /**
     * 文章列表 取消收藏：
     * 1. 不管id是否在收藏列表，都可以取消收藏成功。即存在的id可以重复取消成功，不存在的id也可以取消成功
     * 2. 取消收藏返回的json为{"data":null,"errorCode":0,"errorMsg":""}，故返回值中的ApiArticle是随便写的
     *
     * todo 还有一种常见没有实现：我的收藏页面（该页面包含自己录入的内容）取消收藏
     * todo 网站、网址的收藏、编辑、删除没有实现
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollect(
        @Path("id") id: Int
    ): LiveData<ApiResponse<ApiArticle>>


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
        //如果注册新账号之后没有重新登录，则需要在注册时也要saveCookies
        val isSaveCookies = url.toString().startsWith("https://www.wanandroid.com/user/login?")
                || url.toString().startsWith("https://www.wanandroid.com/user/register?")
        if (isSaveCookies) {
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