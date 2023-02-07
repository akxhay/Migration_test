package com.cb.migrationtest

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [UserRecord::class],
    version = 2
)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {

        const val TABLE_NAME_USER = "user_record"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_NAME = "user_name"

        private var createUserTable = ("create table if not exists " + TABLE_NAME_USER
                + " ( " + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE);")

        private val indices =
            "CREATE UNIQUE INDEX index_user_record_user_name ON user_record(user_name); "
        private const val updateUserName =
            "insert into user_record (id, user_name)   SELECT id,  (FIRST_NAME || ' ' ||   LAST_NAME ) as name from USER ;"

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(createUserTable)
                database.execSQL(indices)
                database.execSQL(updateUserName)
            }
        }
    }
}

