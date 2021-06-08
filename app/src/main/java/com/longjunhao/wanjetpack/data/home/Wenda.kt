package com.longjunhao.wanjetpack.data

import com.google.gson.annotations.SerializedName

/**
 * .WendaResponse
 *
 * @author Admitor
 * @date 2021/05/25
 */

data class Wenda(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("link") val link: String,
    @field:SerializedName("niceDate") val niceDate: String
)
