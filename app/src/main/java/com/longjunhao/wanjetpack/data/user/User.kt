package com.longjunhao.wanjetpack.data.user

import com.google.gson.annotations.SerializedName

/**
 * .Login
 *
 * @author Admitor
 * @date 2021/06/09
 */
data class User(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("admin") val admin: Boolean,
    @field:SerializedName("chapterTops") val chapterTops: List<String>,
    @field:SerializedName("collectIds") val collectIds: List<Int>,
    @field:SerializedName("email") val email: String,
    @field:SerializedName("icon") val icon: String,
    @field:SerializedName("nickname") val nickname: String,
    @field:SerializedName("username") val username: String,
)
