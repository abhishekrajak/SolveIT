package com.github.solveit

import com.github.abhishekrajak.solveit.problem.ProblemGenerator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun scoreTest(){
        for(i in 1..1000){
            val rangeVal = ProblemGenerator.getRange(i)
            assert((rangeVal.first in 1..100)&&(rangeVal.second in 1..100))

        }
    }
}