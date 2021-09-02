package com.example.solveit.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.example.solveit.model.Problem
import kotlin.math.max

class ScoreViewModel(
    app: Application,
    private val sharedPreferences: SharedPreferences
) : AndroidViewModel(app){

    lateinit var problem: Problem
    private var currentScore: Int = 0
    private var maxScore: Int = 0
    private val TAG = "ScopeViewModel"
    private val MAX_SCORE_VAL = "MAX_SCORE_VAL"

    init {
        reset()
    }

    fun reset(){
        currentScore = 0
        loadScore()
    }
    fun loadScore(){
        maxScore = sharedPreferences.getInt(MAX_SCORE_VAL, 0)
    }

    fun incrementCurrentScore(){
        currentScore++
    }

    fun saveScore(){
        if(currentScore > maxScore){
            sharedPreferences.edit().putInt(MAX_SCORE_VAL, currentScore).commit()
        }
    }

    fun getScoreString() : String{
        return (currentScore - maxScore).toString()
    }

    fun resetAllScores(){
        currentScore = 0
        maxScore = 0
        sharedPreferences.edit().putInt(MAX_SCORE_VAL, 0).commit()
    }
}