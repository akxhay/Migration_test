package com.cb.cb_test.application.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cb.migrationtest.UserDao
import com.cb.migrationtest.UserRecord

@Database(
    entities = [UserRecord::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "user_database"
    }
}