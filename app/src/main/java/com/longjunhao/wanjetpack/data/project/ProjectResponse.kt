package com.longjunhao.wanjetpack.data.project

import com.google.gson.annotations.SerializedName
import com.longjunhao.wanjetpack.data.wechat.WechatArticle
import com.longjunhao.wanjetpack.data.wechat.WechatArticlePage

/**
 * .ProjectResponse
 *
 * @author Admitor
 * @date 2021/05/28
 */
data class ProjectResponse(
    @field:SerializedName("data") val data: List<ProjectCategory>,
    @field:SerializedName("errorCode") val errorCode: Int,
    @field:SerializedName("errorMsg") val errorMsg: String
)

data class ProjectCategory(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String
)

data class ProjectArticleResponse(
    @field:SerializedName("data") val data: ProjectArticlePage,
    @field:SerializedName("errorCode") val errorCode: Int,
    @field:SerializedName("errorMsg") val errorMsg: String
)

data class ProjectArticlePage(
    @field:SerializedName("curPage") val curPage: Int,
    @field:SerializedName("datas") val datas: List<ProjectArticle>,
    @field:SerializedName("offset") val offset: Int,
    @field:SerializedName("pageCount") val pageCount: Int,
    @field:SerializedName("size") val size: Int,
    @field:SerializedName("total") val total: Int
)

data class ProjectArticle(
    @field:SerializedName("author") val author: String,
    @field:SerializedName("desc") val desc: String,
    @field:SerializedName("envelopePic") val envelopePic: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("link") val link: String,
    @field:SerializedName("niceDate") val niceDate: String
)
