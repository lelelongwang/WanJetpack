package com.longjunhao.wanjetpack.data.home

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.ApiArticle
import com.longjunhao.wanjetpack.data.AppDatabase
import com.longjunhao.wanjetpack.data.RemoteKey
import retrofit2.HttpException
import java.io.IOException

/**
 * .ArticleRemoteMediator
 *
 * @author Admitor
 * @date 2021/07/05
 *
 * todo 两个问题待优化（以下问题官方demo：PagingWithNetworkSample也会存在问题），期待正式版中能够优化：
 *   1. articleDao.insertAll(data)，插入的数据无序，导致在getLocalArticle()中还要排序
 *   2. 每次执行RemoteMediator.load()中的APPEND后，RecyclerView会整页刷新，且滚动到最顶端。
 *   3. 新安装首次进入界面时，当REFRESH后，在执行APPEND时会加载第二页，导致加载前两页数据。但是下拉刷新执行REFRESH后，再执行APPEND时，就不会加载第二页。
 *
 */

private const val TAG = "RemoteMediator"
private const val HOME_ARTICLE_REMOTE_TYPE = "homeArticle"
private const val HOME_STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val api: WanJetpackApi,
    private val db: AppDatabase
) : RemoteMediator<Int, ApiArticle>() {
    private val articleDao = db.articleDao()
    private val remoteKeyDao = db.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ApiArticle>
    ): MediatorResult {
        try {
            Log.d(TAG, "load: 0 loadType=$loadType")
            val pageKey = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    val remoteKey = db.withTransaction {
                        db.remoteKeyDao().remoteKeyByType(HOME_ARTICLE_REMOTE_TYPE)
                    }
                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    Log.d(TAG, "load: 1 remoteKey=${remoteKey.nextKey}")
                    remoteKey.nextKey
                }
            }

            val page = pageKey ?: HOME_STARTING_PAGE_INDEX

            val data = api.getHomeArticle(page).data.datas
            Log.d(TAG, "load: 2 pageKey=$pageKey  page=$page")

            db.withTransaction {
                if (loadType == REFRESH) {
                    articleDao.deleteArticle()
                    remoteKeyDao.deleteRemoteKeys(HOME_ARTICLE_REMOTE_TYPE)
                }
                articleDao.insertAll(data)
                remoteKeyDao.insert(
                    //RemoteKey(HOME_ARTICLE_REMOTE_TYPE, response.data.curPage)
                    RemoteKey(HOME_ARTICLE_REMOTE_TYPE, page+1)
                )
            }

            return MediatorResult.Success(endOfPaginationReached = data.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}