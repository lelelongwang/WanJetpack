package com.longjunhao.wanjetpack.data.wechat

import com.google.gson.annotations.SerializedName
import com.longjunhao.wanjetpack.data.HomeArticlePage

/**
 * .WechatResponse
 *
 * @author Admitor
 * @date 2021/05/26
 */
data class WechatResponse(
    @field:SerializedName("data") val data: List<Wechat>,
    @field:SerializedName("errorCode") val errorCode: Int,
    @field:SerializedName("errorMsg") val errorMsg: String
)

data class Wechat(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String
)

data class WechatArticleResponse(
    @field:SerializedName("data") val data: WechatArticlePage,
    @field:SerializedName("errorCode") val errorCode: Int,
    @field:SerializedName("errorMsg") val errorMsg: String
)

data class WechatArticlePage(
    @field:SerializedName("curPage") val curPage: Int,
    @field:SerializedName("datas") val datas: List<WechatArticle>,
    @field:SerializedName("offset") val offset: Int,
    @field:SerializedName("pageCount") val pageCount: Int,
    @field:SerializedName("size") val size: Int,
    @field:SerializedName("total") val total: Int
)

data class WechatArticle(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("link") val link: String,
    @field:SerializedName("niceDate") val niceDate: String
)
