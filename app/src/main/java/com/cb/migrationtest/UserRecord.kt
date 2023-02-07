package com.cb.migrationtest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_record",
    indices = [Index(value = ["user_name"], unique = true)]
)
data class UserRecord(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "user_name")
    var userName: String,
)
