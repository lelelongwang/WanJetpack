package com.longjunhao.wanjetpack.data.user

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.ApiArticle

/**
 * .WanjetpackPagingSource
 *
 * @author Admitor
 * @date 2021/05/24
 */

private const val ARTICLE_STARTING_PAGE_INDEX = 0

class CollectionPagingSource(
    private val api: WanJetpackApi
) : PagingSource<Int, ApiArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiArticle> {
        val page = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val response = api.getCollection(page)
            Log.d("CollectionPagingSource", "load: ljh errorCode="+response.errorCode+"  errorMsg="+response.errorMsg+"  data="+response.data)
            //todo 可以在此处加个判断，如果errorCode=-1001，则跳转到登录界面。即使增加了cookie持久化，在此处加上应该算是优化吧
            //如果不做持久化，不管当前应用是否登录，此处返回的都是errorCode=-1001。即认为没有登录。
            val datas = response.data.datas
            LoadResult.Page(
                data = datas,
                prevKey = if (page == ARTICLE_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.data.pageCount) null else page + 1,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ApiArticle>): Int? {
        return null
    }
}