package com.longjunhao.wanjetpack.data.project

import androidx.paging.PagingSource
import com.longjunhao.wanjetpack.api.WanJetpackApi

/**
 * .ProjectPagingSource
 *
 * @author Admitor
 * @date 2021/05/31
 */

private const val PROJECT_STARTING_PAGE_INDEX = 1

class ProjectPagingSource(
    private val api: WanJetpackApi,
    private val query: Int
) : PagingSource<Int, ProjectArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProjectArticle> {
        val page = params.key ?: PROJECT_STARTING_PAGE_INDEX
        return try {
            val response = api.getProjectArticle(page, query)
            val photos = response.data.datas
            LoadResult.Page(
                data = photos,
                prevKey = if (page == PROJECT_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.data.pageCount) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}