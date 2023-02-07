package com.cb.migrationtest

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val dao: UserDao) : UserRepository {
    val users: Flow<List<UserRecord>> = dao.getUsers()

    override suspend fun insertUser(userRecord: UserRecord) = dao.insertUser(userRecord)

    override suspend fun deleteUser(id: Int?) = dao.deleteUser(id)


}