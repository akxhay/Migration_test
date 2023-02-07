package com.cb.migrationtest

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertUser(userRecord: UserRecord): Long
    suspend fun deleteUser(id: Int?)
}