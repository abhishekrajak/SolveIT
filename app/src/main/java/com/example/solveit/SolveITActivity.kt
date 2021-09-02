package com.example.solveit

import android.content.SharedPreferences
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.solveit.databinding.ActivityMainBinding
import com.example.solveit.model.Operator
import com.example.solveit.problem.ProblemGenerator
import com.example.solveit.viewModel.ScoreViewModel
import kotlinx.coroutines.*

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat


class SolveITActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"
    lateinit var scoreViewModel: ScoreViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreference: SharedPreferences
    private var sharedPrefName = "solveITSharedPref"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setTheme(R.style.SolveIT)
        setContentView(view)

        sharedPreference = getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val scoreViewModelProviderFactory =
            ScoreViewModelProviderFactory(application, sharedPreference)
        scoreViewModel =
            ViewModelProvider(this, scoreViewModelProviderFactory).get(ScoreViewModel::class.java)

        binding.appName.text = getString(R.string.appName)

        binding.resetScore.setOnClickListener {
            scoreViewModel.resetAllScores()
            setupScore()
            binding.resetScore.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.avd_reload_anim
                )
            )

            val drawable = binding.resetScore.drawable
            if (drawable is AnimatedVectorDrawableCompat) {
                drawable.start()
            } else if (drawable is AnimatedVectorDrawable) {
                drawable.start()
            }

        }
        updateUIScore()
        CoroutineScope(Dispatchers.Unconfined).launch {
            setProblem()
        }
        setupListener()
        setupScore()
    }

    private fun setupScore() {
        binding.score.text = scoreViewModel.getScoreString()
    }

    private val buttonClick = AlphaAnimation(1f, 0.0f)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupListener() {
        binding.answer.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && !binding.answer.text.isEmpty()) {
                checkAnswer()
                return@OnEditorActionListener true
            }
            false
        })

        binding.button.setOnClickListener {
            it.startAnimation(buttonClick)
            if (!binding.answer.text.isEmpty()) {
                checkAnswer()
            }
        }
        binding.button.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundBlue))
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun checkAnswer() {
        CoroutineScope(Dispatchers.Unconfined).launch {
            val input = binding.answer.text.toString().toLong()
            if (input.equals(scoreViewModel.problem.answer)) {
                scoreViewModel.incrementCurrentScore()
                updateUIScore()
                binding.ivCorrectIcon.visibility = View.VISIBLE

                val drawable = binding.ivCorrectIcon.drawable
                if (drawable is AnimatedVectorDrawableCompat) {
                    drawable.start()
                } else if (drawable is AnimatedVectorDrawable) {
                    drawable.start()
                }
                CoroutineScope(Dispatchers.Unconfined).launch {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        disableIcon()
                    }
                }

            } else {
                scoreViewModel.saveScore()
                updateUIScore()
                scoreViewModel.reset()
                binding.ivWrongIcon.visibility = View.VISIBLE

                val drawable = binding.ivWrongIcon.drawable
                if (drawable is AnimatedVectorDrawableCompat) {
                    drawable.start()
                } else if (drawable is AnimatedVectorDrawable) {
                    drawable.start()
                }
                CoroutineScope(Dispatchers.Unconfined).launch {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        disableIcon()
                    }
                }
            }
            setProblem()
            setupScore()
            binding.answer.setText("")
        }
    }

    private suspend fun disableIcon() {
        binding.ivCorrectIcon.visibility = View.INVISIBLE
        binding.ivWrongIcon.visibility = View.INVISIBLE
    }

    private fun updateUIScore() {

    }

    private suspend fun setProblem() {
        scoreViewModel.problem = ProblemGenerator.generate()
        withContext(Dispatchers.Main) {
            binding.operand1.text = scoreViewModel.problem.operand1.toString()
            binding.operand2.text = scoreViewModel.problem.operand2.toString()

            val operatorSwitch = when (scoreViewModel.problem.operator) {
                Operator.addition -> "+"
                Operator.subtraction -> "-"
                Operator.multiplication -> "×"
                else -> "+"
            }
            binding.operator.text = operatorSwitch
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scoreViewModel.saveScore()
    }
}