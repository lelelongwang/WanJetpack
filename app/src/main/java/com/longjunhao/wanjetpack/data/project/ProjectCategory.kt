package com.longjunhao.wanjetpack.data.project

import com.google.gson.annotations.SerializedName

/**
 * .ProjectResponse
 *
 * @author Admitor
 * @date 2021/05/28
 */

data class ProjectCategory(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    var isSelected: Boolean
)
