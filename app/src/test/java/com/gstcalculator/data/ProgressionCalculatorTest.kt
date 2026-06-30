package com.gstcalculator.data

import com.gstcalculator.domain.model.Difficulty
import com.gstcalculator.domain.model.GameStatus
import com.gstcalculator.engine.GSTCalculatorEngine
import com.gstcalculator.engine.GSTCalculatorGenerator
import com.gstcalculator.util.ProgressionCalculator
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressionCalculatorTest {

    @Test
    fun xpForCompletedGame_isPositive() {
        val level = GSTCalculatorGenerator.generate(1L, 1, Difficulty.EASY)
        val game = GSTCalculatorEngine.createInitialGame(level).copy(status = GameStatus.COMPLETED)
        assertTrue(ProgressionCalculator.xpForGame(game) > 0)
    }

    @Test
    fun xpForGame_withHints_isLowerThanWithoutHints() {
        val level = GSTCalculatorGenerator.generate(1L, 1, Difficulty.EASY)
        val withHints = GSTCalculatorEngine.createInitialGame(level).copy(hintsUsed = 2, status = GameStatus.COMPLETED)
        val noHints = GSTCalculatorEngine.createInitialGame(level).copy(hintsUsed = 0, status = GameStatus.COMPLETED)
        assertTrue(ProgressionCalculator.xpForGame(noHints) >= ProgressionCalculator.xpForGame(withHints))
    }
}
