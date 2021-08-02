package com.example.solveit.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.io.Serializable

@Entity(tableName = "Score_table")
data class Score(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "scoreVal")
    @NonNull
    var scoreVal: Int? = null,

    @ColumnInfo(name = "date")
    @NonNull
    var date: Date? = null

) : Serializable