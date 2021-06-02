package com.longjunhao.wanjetpack.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.home.HomeArticlePagingSource
import com.longjunhao.wanjetpack.data.home.WendaPagingSource
import com.longjunhao.wanjetpack.data.project.ProjectArticle
import com.longjunhao.wanjetpack.data.project.ProjectPagingSource
import com.longjunhao.wanjetpack.data.project.ProjectResponse
import com.longjunhao.wanjetpack.data.wechat.WechatArticle
import com.longjunhao.wanjetpack.data.wechat.WechatArticlePagingSource
import com.longjunhao.wanjetpack.data.wechat.WechatResponse
import kotlinx.coroutines.Deferred
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
    }

    fun getHomeArticle(): Flow<PagingData<HomeArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = HOME_ARTICLE_PAGE_SIZE),
            pagingSourceFactory = { HomeArticlePagingSource(api) }
        ).flow
    }

    fun getWenda(): Flow<PagingData<Wenda>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = WENDA_PAGE_SIZE),
            pagingSourceFactory = { WendaPagingSource(api)}
        ).flow
    }

    fun getProjectCategory(): Deferred<ProjectResponse> {
        return api.getProjectCategory()
    }

    fun getProjectArticle(categoryId: Int): Flow<PagingData<ProjectArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PROJECT_PAGE_SIZE),
            pagingSourceFactory = { ProjectPagingSource(api, categoryId) }
        ).flow
    }

    fun getWechatName(): Deferred<WechatResponse> {
        return api.getWechatName()
    }

    fun getWechatArticle(wechatId: Int): Flow<PagingData<WechatArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = WECHAT_PAGE_SIZE),
            pagingSourceFactory = { WechatArticlePagingSource(api, wechatId) }
        ).flow
    }

}