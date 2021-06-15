package com.longjunhao.wanjetpack.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.home.HomeArticlePagingSource
import com.longjunhao.wanjetpack.data.home.WendaPagingSource
import com.longjunhao.wanjetpack.data.project.ProjectCategory
import com.longjunhao.wanjetpack.data.project.ProjectPagingSource
import com.longjunhao.wanjetpack.data.user.CollectionPagingSource
import com.longjunhao.wanjetpack.data.user.User
import com.longjunhao.wanjetpack.data.wechat.WechatCategory
import com.longjunhao.wanjetpack.data.wechat.WechatArticlePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * .WanJetpackRepository
 *
 * @author Admitor
 * @date 2021/05/24
 */
@Singleton
class WanJetpackRepository @Inject constructor(
    private val api: WanJetpackApi
) {
    companion object {
        private const val HOME_ARTICLE_PAGE_SIZE = 20
        private const val WENDA_PAGE_SIZE = 23
        private const val PROJECT_PAGE_SIZE = 18
        private const val WECHAT_PAGE_SIZE = 20
        private const val COLLECTION_ARTICLE_PAGE_SIZE = 20
    }

    fun getHomeArticle(): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = HOME_ARTICLE_PAGE_SIZE),
            pagingSourceFactory = { HomeArticlePagingSource(api) }
        ).flow
    }

    fun getWenda(): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = WENDA_PAGE_SIZE),
            pagingSourceFactory = { WendaPagingSource(api)}
        ).flow
    }

    fun getProjectCategory(): LiveData<ApiResponse<List<ProjectCategory>>> {
        return api.getProjectCategory()
    }

    fun getProjectArticle(categoryId: Int): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PROJECT_PAGE_SIZE),
            pagingSourceFactory = { ProjectPagingSource(api, categoryId) }
        ).flow
    }

    fun getWechatName(): LiveData<ApiResponse<List<WechatCategory>>> {
        return api.getWechatName()
    }

    fun getWechatArticle(wechatId: Int): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = WECHAT_PAGE_SIZE),
            pagingSourceFactory = { WechatArticlePagingSource(api, wechatId) }
        ).flow
    }

    fun login(username: String, password: String): LiveData<ApiResponse<User>> {
        Log.d("WanJetpackRepository", "login: ljh name=$username  pass=$password")
        return api.login(username, password)
    }

    fun register(username: String, password: String, repassword: String): LiveData<ApiResponse<User>> {
        return api.register(username, password, repassword)
    }

    fun logout(): LiveData<ApiResponse<User>>{
        return api.logout()
    }

    fun getCollectionArticle(): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = COLLECTION_ARTICLE_PAGE_SIZE),
            pagingSourceFactory = { CollectionPagingSource(api) }
        ).flow
    }

}