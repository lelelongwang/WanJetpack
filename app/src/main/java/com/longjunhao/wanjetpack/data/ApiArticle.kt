package com.longjunhao.wanjetpack.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * .Article
 *
 * @author Admitor
 * @date 2021/06/11
 */
@Entity(tableName = "article")
data class ApiArticle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("originId") val originId: Int,

    @field:SerializedName("author") val author: String,
    @field:SerializedName("shareUser") val shareUser: String,
    /**
     * 注意 collect 是 var 类型
     */
    @field:SerializedName("collect") var collect: Boolean,
    @field:SerializedName("desc") val desc: String,
    @field:SerializedName("envelopePic") val envelopePic: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("link") val link: String,
    @field:SerializedName("niceDate") val niceDate: String
)
