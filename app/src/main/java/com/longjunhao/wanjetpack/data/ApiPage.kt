package com.longjunhao.wanjetpack.data

import com.google.gson.annotations.SerializedName

/**
 * .WanJetResponse
 *
 * @author Admitor
 * @date 2021/06/08
 */

data class ApiPage<T>(
    @field:SerializedName("curPage") val curPage: Int,
    @field:SerializedName("datas") val datas: List<T>,
    @field:SerializedName("offset") val offset: Int,
    @field:SerializedName("pageCount") val pageCount: Int,
    @field:SerializedName("size") val size: Int,
    @field:SerializedName("total") val total: Int
)
