package com.longjunhao.wanjetpack.data.wechat

import com.google.gson.annotations.SerializedName

/**
 * .WechatArticle
 *
 * @author Admitor
 * @date 2021/06/08
 */
data class WechatArticle(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("link") val link: String,
    @field:SerializedName("niceDate") val niceDate: String
)
