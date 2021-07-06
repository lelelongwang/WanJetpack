package com.longjunhao.wanjetpack.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * .ArticleDao
 *
 * @author Admitor
 * @date 2021/07/05
 */

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ApiArticle>)

    //@Query("SELECT * FROM article")
    @Query("SELECT * FROM article ORDER BY niceDate ASC")
    fun getLocalArticle(): PagingSource<Int, ApiArticle>

    @Query("DELETE FROM article")
    suspend fun deleteArticle()
}