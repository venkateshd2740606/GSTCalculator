package com.gstcalculator.engine

import com.gstcalculator.domain.model.GSTCalculatorLevel
import com.gstcalculator.domain.model.Difficulty

object TutorialLevels {

    val all: List<GSTCalculatorLevel> = listOf(
        level1Basics(),
        level2TwoColors(),
        level3HiddenOrder(),
        level4Stacking(),
        level5Challenge()
    )

    fun getTutorialLevel(index: Int): GSTCalculatorLevel? = all.getOrNull(index)

    private fun level1Basics(): GSTCalculatorLevel = GSTCalculatorLevel(
        seed = 1,
        levelNumber = 1,
        difficulty = Difficulty.BEGINNER,
        isTutorial = true,
        colorCount = 2,
        initialTubes = listOf(
            listOf(0, 1),
            listOf(1, 0),
            emptyList()
        )
    )

    private fun level2TwoColors(): GSTCalculatorLevel = GSTCalculatorLevel(
        seed = 2,
        levelNumber = 2,
        difficulty = Difficulty.BEGINNER,
        isTutorial = true,
        colorCount = 2,
        initialTubes = listOf(
            listOf(0, 0, 1),
            listOf(1, 1, 0),
            emptyList()
        )
    )

    private fun level3HiddenOrder(): GSTCalculatorLevel = GSTCalculatorLevel(
        seed = 3,
        levelNumber = 3,
        difficulty = Difficulty.EASY,
        isTutorial = true,
        colorCount = 3,
        initialTubes = listOf(
            listOf(0, 1, 2),
            listOf(2, 0, 1),
            listOf(1, 2, 0),
            emptyList()
        )
    )

    private fun level4Stacking(): GSTCalculatorLevel = GSTCalculatorLevel(
        seed = 4,
        levelNumber = 4,
        difficulty = Difficulty.EASY,
        isTutorial = true,
        colorCount = 3,
        initialTubes = listOf(
            listOf(0, 1, 1, 2),
            listOf(2, 0, 0, 1),
            listOf(1, 2, 2, 0),
            emptyList(),
            emptyList()
        )
    )

    private fun level5Challenge(): GSTCalculatorLevel = GSTCalculatorLevel(
        seed = 5,
        levelNumber = 5,
        difficulty = Difficulty.MEDIUM,
        isTutorial = true,
        colorCount = 4,
        initialTubes = listOf(
            listOf(0, 1, 2, 3),
            listOf(3, 0, 1, 2),
            listOf(2, 3, 0, 1),
            listOf(1, 2, 3, 0),
            emptyList(),
            emptyList()
        )
    )
}
