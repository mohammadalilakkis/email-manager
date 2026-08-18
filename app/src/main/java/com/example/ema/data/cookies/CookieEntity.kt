package com.example.ema.data.cookies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "cookies")
data class CookieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "value")
    var value: String,

    @ColumnInfo(name = "created_at")
    var createAt: Timestamp
)
