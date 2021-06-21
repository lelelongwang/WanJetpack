package com.longjunhao.wanjetpack.data.home

import androidx.paging.PagingSource
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.ApiArticle

/**
 * .WendaPagingSource
 *
 * @author Admitor
 * @date 2021/06/21
 */

private const val SEARCH_STARTING_PAGE_INDEX = 0

class SearchPagingSource(
    private val api: WanJetpackApi,
    private val keyword: String
) : PagingSource<Int, ApiArticle>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiArticle> {
        val page = params.key ?: SEARCH_STARTING_PAGE_INDEX
        return try {
            val response = api.search(page, keyword)
            val datas = response.data.datas
            LoadResult.Page(
                data = datas,
                prevKey = if (page == SEARCH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.data.pageCount) null else page + 1,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}