package com.longjunhao.wanjetpack.data.wechat

import androidx.paging.PagingSource
import com.longjunhao.wanjetpack.api.WanJetpackApi

/**
 * .WechatArticlePagingSource
 *
 * @author Admitor
 * @date 2021/05/28
 */

private const val WECHAT_STARTING_PAGE_INDEX = 1

class WechatArticlePagingSource(
    private val api: WanJetpackApi,
    private val wechatId: Int
) : PagingSource<Int,WechatArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WechatArticle> {
        val page = params.key ?: WECHAT_STARTING_PAGE_INDEX
        return try {
            val response = api.getWechatArticle(wechatId, page)
            val datas = response.data.datas
            LoadResult.Page(
                data = datas,
                prevKey = if (page == WECHAT_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.data.pageCount) null else page + 1,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}