package com.longjunhao.wanjetpack.api

import androidx.lifecycle.LiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.longjunhao.wanjetpack.data.HomeArticleResponse
import com.longjunhao.wanjetpack.data.WendaResponse
import com.longjunhao.wanjetpack.data.project.ProjectArticleResponse
import com.longjunhao.wanjetpack.data.project.ProjectResponse
import com.longjunhao.wanjetpack.data.wechat.Wechat
import com.longjunhao.wanjetpack.data.wechat.WechatArticleResponse
import com.longjunhao.wanjetpack.data.wechat.WechatResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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
    ): HomeArticleResponse

    /**
     * 获取问答列表
     */
    @GET("/wenda/list/{page}/json")
    suspend fun getWenda(
        @Path("page") page: Int
    ): WendaResponse

    /**
     * 获取项目分类列表
     */
    @GET("/project/tree/json")
    fun getProjectCategory(): Deferred<ProjectResponse>

    /**
     * 获取项目列表数据
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectArticle(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ProjectArticleResponse

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/chapters/json")
    fun getWechatName(): Deferred<WechatResponse>

    /**
     * 获取某个公众号历史数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticle(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): WechatArticleResponse


    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        fun create(): WanJetpackApi {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(WanJetpackApi::class.java)
        }
    }
}