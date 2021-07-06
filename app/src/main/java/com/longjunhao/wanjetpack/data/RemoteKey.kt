package com.longjunhao.wanjetpack.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * .RemoteKey
 *
 * @author Admitor
 * @date 2021/07/05
 */
@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    @ColumnInfo(name = "articleType",collate = ColumnInfo.NOCASE)
    val articleType: String,
    val nextKey: Int?
)
