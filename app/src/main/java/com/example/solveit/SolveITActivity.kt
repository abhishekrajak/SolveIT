package com.example.solveit

import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.solveit.databinding.ActivityMainBinding
import com.example.solveit.db.ScoreDatabase
import com.example.solveit.model.Score
import com.example.solveit.problem.Operator
import com.example.solveit.problem.ProblemGenerator
import com.example.solveit.repository.ScoreRepository
import com.example.solveit.viewModel.ScoreViewModel
import kotlinx.coroutines.*

class SolveITActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"
    lateinit var scoreViewModel: ScoreViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setTheme(R.style.Theme_SolveIT)
        setContentView(view)

        val scoreRepository = ScoreRepository(ScoreDatabase(this))
        val scoreViewModelProviderFactory = ScoreViewModelProviderFactory(application, scoreRepository)
        scoreViewModel = ViewModelProvider(this, scoreViewModelProviderFactory).get(ScoreViewModel::class.java)

        binding.appName.text = getString(R.string.appName)
        updateUIScore()
        CoroutineScope(Dispatchers.Unconfined).launch {
            setProblem()
        }
        setButtonProperty()
        setUIUpdateOnDatabase()
    }

    private fun setUIUpdateOnDatabase(){
        val observableItem = scoreViewModel.getScores()
        observableItem.observeForever(object : Observer<List<Score>> {
            override fun onChanged(t: List<Score>?) {
                updateUIScore()
            }
        })
    }

    private fun setButtonProperty(){
        binding.submit.isEnabled = false

        binding.answer.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {
                binding.submit.isEnabled = s.toString().trim{ it <= ' ' }.isNotEmpty()
            }
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.submit.setOnClickListener {
            CoroutineScope(Dispatchers.Unconfined).launch {
                val input = binding.answer.text.toString().toLong()
                if(input.equals(scoreViewModel.problem.answer)){
//                    Toast.makeText(applicationContext, "Correct", Toast.LENGTH_SHORT).show()
                    scoreViewModel.incrementCurrentScore()
                    updateUIScore()
                    binding.ivCorrectIcon.visibility = View.VISIBLE
                    CoroutineScope(Dispatchers.Unconfined).launch {
                        delay(1000)
                        withContext(Dispatchers.Main){
                            disableIcon()
                        }
                    }

                }else{
//                    Toast.makeText(applicationContext, "Incorrect", Toast.LENGTH_SHORT).show()
                    scoreViewModel.saveScore()
                    scoreViewModel.resetCurrentScore()
                    updateUIScore()
                    binding.ivWrongIcon.visibility = View.VISIBLE
                    CoroutineScope(Dispatchers.Unconfined).launch {
                        delay(1000)
                        withContext(Dispatchers.Main){
                            disableIcon()
                        }
                    }
                }
                setProblem()
                binding.answer.setText("")
            }
        }
    }

    private suspend fun disableIcon(){
        binding.ivCorrectIcon.visibility = View.INVISIBLE
        binding.ivWrongIcon.visibility = View.INVISIBLE
    }

    private fun updateUIScore(){
        binding.score.text = scoreViewModel.getScoreString()
    }

    private suspend fun setProblem(){
        scoreViewModel.problem = ProblemGenerator.generate()
        withContext(Dispatchers.Main){
            binding.operand1.text = scoreViewModel.problem.operand1.toString()
            binding.operand2.text = scoreViewModel.problem.operand2.toString()

            val operatorSwitch = when(scoreViewModel.problem.operator){
                Operator.addition -> "+"
                Operator.subtraction -> "-"
                Operator.multiplication -> "X"
                else -> "+"
            }
            binding.operator.text = operatorSwitch
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        scoreViewModel.saveScore()
    }
}
