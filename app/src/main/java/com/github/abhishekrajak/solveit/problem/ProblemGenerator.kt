package com.github.abhishekrajak.solveit.problem

import com.github.abhishekrajak.solveit.model.Operator
import com.github.abhishekrajak.solveit.model.Problem
import com.github.abhishekrajak.solveit.util.Constants.Companion.NINETY_NINE
import com.github.abhishekrajak.solveit.util.Constants.Companion.ONE
import com.github.abhishekrajak.solveit.util.Constants.Companion.TEN
import com.github.abhishekrajak.solveit.util.Constants.Companion.THREE
import com.github.abhishekrajak.solveit.util.Constants.Companion.ZERO
import kotlin.math.min

class ProblemGenerator {
    companion object{
        fun generate() : Problem {
            val operand1 = (ONE..TEN).random().toLong()
            val operand2 = (ONE..TEN).random().toLong()

            val option = (ONE..THREE).random().toInt()
            val operator = getOperator(option)

            return Problem(operand1 = operand1, operand2 = operand2, operator = operator,
                answer = getAnswer(operand1 = operand1, operand2 = operand2, operator = operator)
            )
        }

        fun generateProblemAccordingToScore(currentScore: Int) : Problem{
            val range = getRange(currentScore)
            val operand1 = (range.first..range.second).random().toLong()
            val operand2 = (range.first..range.second).random().toLong()

            val option = (ONE..THREE).random()
            val operator = getOperator(option)

            return Problem(operand1 = operand1, operand2 = operand2, operator = operator,
                answer = getAnswer(operand1 = operand1, operand2 = operand2, operator = operator))
        }

        fun getRange(currentScore: Int) : Pair<Int, Int>{
            val y = TEN*(currentScore/TEN)
            return Pair(min(if(y == ZERO) ONE else y, NINETY_NINE), min(TEN*(currentScore/TEN+ONE), NINETY_NINE))
        }

        private fun getOperator(option:Int) : Operator {
            return when(option){
                1 -> Operator.addition
                2 -> Operator.subtraction
                3 -> Operator.multiplication
                else -> Operator.addition
            }
        }

        private fun getAnswer(operand1: Long, operand2: Long, operator: Operator) : Long{
            return when(operator){
                Operator.addition -> operand1 + operand2
                Operator.subtraction -> operand1 - operand2
                Operator.multiplication -> operand1 * operand2
            }
        }

    }
}