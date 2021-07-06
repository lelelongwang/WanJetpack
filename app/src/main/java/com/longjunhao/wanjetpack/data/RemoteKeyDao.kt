package com.longjunhao.wanjetpack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * .RemoteKeyDao
 *
 * @author Admitor
 * @date 2021/07/05
 */
@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys WHERE articleType = :type")
    suspend fun remoteKeyByType(type: String): RemoteKey

    @Query("DELETE FROM remote_keys WHERE articleType = :type")
    suspend fun deleteRemoteKeys(type: String)
}