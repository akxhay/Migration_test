package com.cb.migrationtest

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_record ORDER BY id ASC")
    fun getUsers(): Flow<List<UserRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userRecord: UserRecord?): Long


    @Query("DELETE FROM user_record WHERE id = :id")
    suspend fun deleteUser(id: Int?)


}