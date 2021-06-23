package com.longjunhao.wanjetpack.data.home

import com.google.gson.annotations.SerializedName

/**
 * .ApiBanner
 *
 * @author Admitor
 * @date 2021/06/22
 */

data class ApiBanner(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("imagePath") val imagePath: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("url") val url: String
)
