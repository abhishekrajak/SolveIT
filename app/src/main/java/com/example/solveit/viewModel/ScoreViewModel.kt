package com.example.solveit.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.solveit.db.Converters
import com.example.solveit.model.Score
import com.example.solveit.problem.Problem
import com.example.solveit.problem.ProblemGenerator
import com.example.solveit.repository.ScoreRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class ScoreViewModel(
    app: Application,
    private val scoreRepository: ScoreRepository
) : AndroidViewModel(app){

    lateinit var problem: Problem
    private var currentScore: Int = 0
    private var maxScore: Int = 0
    private val TAG = "ScopeViewModel"

    init {
        Log.d(TAG, "Init")

//        viewModelScope.launch {
//            val triggerScore = Score(scoreVal = 0, date = Converters().fromTimestamp(Date().time))
//            val rowId = scoreRepository.insert(triggerScore)
//            triggerScore.id = rowId.toInt()
//            Log.d(TAG, "rowId: ${rowId}")
//            delay(5000)
//            scoreRepository.delete(triggerScore)
//        }

        val observableItem = getScores()
        observableItem.observeForever(object : Observer<List<Score>> {
            override fun onChanged(t: List<Score>?) {
                t?.forEach{
                    Log.d(TAG, "onChanged: ${it.id} ${it.scoreVal} ${it.date}")
                    try {
                        val x = it.scoreVal!!
                        if(x > maxScore){
                            maxScore = x
                        }
                    }   catch (e : Exception){}
                }
                Log.d(TAG, "onChanged: ------------------------Display Over--------------------------------")
            }

        })

//        CoroutineScope(Dispatchers.IO).launch {
//            scoreRepository.deleteAll()
//        }

    }

    fun incrementCurrentScore(){
        currentScore++
    }

    fun resetCurrentScore(){
        currentScore = 0
    }

    fun saveScore(){
        Log.d(TAG, "saveScore: ${currentScore}" )
        val score = Score(scoreVal = currentScore, date = Converters().fromTimestamp(Date().time))
        val rowId = insertScore(score)

        if(currentScore > maxScore){
            maxScore = currentScore
        }

        Log.d(TAG, "saveScore: ${rowId}")
    }

    fun check(input: Long) : Boolean{
        return input == problem.answer
    }

    suspend fun problemGenerate() : Problem{
        return ProblemGenerator.generate()
    }

    fun getScoreString() : String{
        return String.format("Current : %d (Max : %d)", currentScore, maxScore)
    }

    fun insertScore(score: Score) = viewModelScope.launch {
        scoreRepository.insert(score)
    }

    fun getScores() = scoreRepository.getScores()

    fun deleteScore(score: Score) = viewModelScope.launch {
        scoreRepository.delete(score)
    }

}