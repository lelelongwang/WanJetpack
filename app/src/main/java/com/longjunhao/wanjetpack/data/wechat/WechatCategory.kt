package com.longjunhao.wanjetpack.data.wechat

import com.google.gson.annotations.SerializedName

/**
 * .WechatResponse
 *
 * @author Admitor
 * @date 2021/05/26
 */

data class WechatCategory(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String
)
