package com.gstcalculator.engine

import com.gstcalculator.domain.model.Difficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GSTCalculatorEngineTest {

    @Test
    fun tutorialLevel_isValidAndSolvable() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        assertTrue(GSTCalculatorEngine.validateLevel(level))
    }

    @Test
    fun pour_updatesTubeState() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        var game = GSTCalculatorEngine.createInitialGame(level)
        assertTrue(GSTCalculatorEngine.canPour(game, 0, 2))
        game = GSTCalculatorEngine.pour(game, 0, 2)
        assertEquals(1, game.moves)
        assertTrue(game.tubes[2].isNotEmpty())
    }

    @Test
    fun solveTutorial_completesGame() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        var game = GSTCalculatorEngine.createInitialGame(level)
        val solution = GSTCalculatorEngine.solve(game)!!
        solution.forEach { (from, to) ->
            game = GSTCalculatorEngine.pour(game, from, to)
        }
        assertTrue(GSTCalculatorEngine.isWon(game))
    }

    @Test
    fun generatedLevel_isValid() {
        val level = GSTCalculatorGenerator.generate(12345L, 1, Difficulty.EASY)
        assertTrue(GSTCalculatorEngine.validateLevel(level))
    }

    @Test
    fun tubeSelection_poursWhenSecondTubeSelected() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        var game = GSTCalculatorEngine.createInitialGame(level)
        game = GSTCalculatorEngine.onTubeSelected(game, 0)
        assertEquals(0, game.selectedTubeId)
        game = GSTCalculatorEngine.onTubeSelected(game, 2)
        assertEquals(1, game.moves)
    }

    @Test
    fun generator_sameSeed_producesSameLevel() {
        val a = GSTCalculatorGenerator.generate(999L, 5, Difficulty.MEDIUM)
        val b = GSTCalculatorGenerator.generate(999L, 5, Difficulty.MEDIUM)
        assertEquals(a.initialTubes, b.initialTubes)
    }
}
