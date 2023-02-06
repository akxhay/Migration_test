package com.cb.migrationtest

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var createUserTable = ("create table if not exists " + TABLE_NAME_USER
            + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " VARCHAR, "
            + COLUMN_LAST_NAME + " VARCHAR ,"
            + " UNIQUE(" + COLUMN_FIRST_NAME + "," + COLUMN_LAST_NAME + "));")

    private lateinit var database: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createUserTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Db not upgraded yet
    }

    fun insertUser(userEntity: UserEntity): Long {
        database = this.writableDatabase
        val contentValues = getContentValuesFromUserEntity(userEntity)
        return database.insert(TABLE_NAME_USER, null, contentValues).also { database.close() }
    }


    fun getUsers(): ArrayList<UserEntity> {
        val list = ArrayList<UserEntity>()
        database = this.readableDatabase
        val c = database.rawQuery("SELECT * FROM $TABLE_NAME_USER ORDER BY $COLUMN_ID DESC", null)
        while (c.moveToNext()) {
            list.add(getUserFromCursor(c))
        }
        c.close()
        database.close()
        return list
    }


    private fun getUserFromCursor(c: Cursor): UserEntity {
        val userEntity = UserEntity()
        userEntity.id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID))
        userEntity.firstName = c.getString(c.getColumnIndexOrThrow(COLUMN_FIRST_NAME))
        userEntity.lastName = c.getString(c.getColumnIndexOrThrow(COLUMN_LAST_NAME))
        return userEntity
    }

    private fun getContentValuesFromUserEntity(userEntity: UserEntity): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_FIRST_NAME, userEntity.firstName)
        contentValues.put(COLUMN_LAST_NAME, userEntity.lastName)
        return contentValues
    }


    fun deleteUser(id: Int) {
        database = this.writableDatabase
        database.delete(TABLE_NAME_USER, "$COLUMN_ID = $id", null)
        database.close()
    }


    companion object {
        const val DATABASE_NAME = "UserDatabase.db"

        const val TABLE_NAME_USER = "USER"
        const val COLUMN_ID = "ID"
        const val COLUMN_FIRST_NAME = "FIRST_NAME"
        const val COLUMN_LAST_NAME = "LAST_NAME"

        private const val DATABASE_VERSION = 1
    }
}