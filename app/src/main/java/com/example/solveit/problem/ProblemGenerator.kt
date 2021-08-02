package com.example.solveit.problem

class ProblemGenerator {
    companion object{
        fun generate() : Problem{
            val operand1 = (1..10).random().toLong()
            val operand2 = (1..10).random().toLong()

            val option = (1..3).random().toInt()
            val operator = getOperator(option)

            return Problem(operand1 = operand1, operand2 = operand2, operator = operator,
                answer = getAnswer(operand1 = operand1, operand2 = operand2, operator = operator))
        }

        fun getOperator(option:Int) : Operator{
            return when(option){
                1 -> Operator.addition
                2 -> Operator.subtraction
                3 -> Operator.multiplication
                else -> Operator.addition
            }
        }

        fun getAnswer(operand1: Long, operand2: Long, operator: Operator) : Long{
            return when(operator){
                Operator.addition -> operand1 + operand2
                Operator.subtraction -> operand1 - operand2
                Operator.multiplication -> operand1 * operand2
                else -> operand1 + operand2
            }
        }

    }
}