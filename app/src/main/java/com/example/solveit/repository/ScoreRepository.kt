package com.example.solveit.repository

import com.example.solveit.db.ScoreDatabase
import com.example.solveit.model.Score

class ScoreRepository(val db: ScoreDatabase) {
    suspend fun insert(score: Score) : Long{
        return db.getScoreDao().insertScore(score)
    }

    fun getScores() = db.getScoreDao().getScores()

    suspend fun delete(score: Score){
        db.getScoreDao().deleteScore(score)
    }

    suspend fun deleteAll(){
        db.getScoreDao().deleteAll()
    }
}