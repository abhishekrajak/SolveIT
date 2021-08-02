package com.example.solveit.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.solveit.model.Score

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score) : Long

    @Query("SELECT * from score_table")
    fun getScores() : LiveData<List<Score>>

    @Delete
    suspend fun deleteScore(score: Score)

    @Query("DELETE FROM score_table")
    fun deleteAll()

}