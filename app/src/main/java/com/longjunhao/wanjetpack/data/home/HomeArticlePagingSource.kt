package com.longjunhao.wanjetpack.data.home

import androidx.paging.PagingSource
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.ApiArticle

/**
 * .WanjetpackPagingSource
 *
 * @author Admitor
 * @date 2021/05/24
 */

private const val ARTICLE_STARTING_PAGE_INDEX = 0

class HomeArticlePagingSource(
    private val api: WanJetpackApi
) : PagingSource<Int, ApiArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiArticle> {
        val page = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val response = api.getHomeArticle(page)
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
}