package com.gstcalculator.domain.repository

import com.gstcalculator.domain.model.Achievement
import com.gstcalculator.domain.model.ChallengeRecord
import com.gstcalculator.domain.model.ChallengeType
import com.gstcalculator.domain.model.GSTCalculatorGame
import com.gstcalculator.domain.model.GSTCalculatorLevel
import com.gstcalculator.domain.model.Difficulty
import com.gstcalculator.domain.model.EconomyState
import com.gstcalculator.domain.model.PuzzleProfile
import com.gstcalculator.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun createNewGame(difficulty: Difficulty, levelNumber: Int): GSTCalculatorGame
    suspend fun createGameFromSeed(seed: Long, levelNumber: Int, difficulty: Difficulty): GSTCalculatorGame
    suspend fun createTutorialGame(tutorialIndex: Int): GSTCalculatorGame?
    suspend fun createEndlessGame(wave: Int): GSTCalculatorGame
    suspend fun saveGame(game: GSTCalculatorGame): Long
    suspend fun getGame(gameId: Long): GSTCalculatorGame?
    suspend fun getInProgressGame(): GSTCalculatorGame?
    fun observeInProgressGame(): Flow<GSTCalculatorGame?>
    suspend fun completeGame(game: GSTCalculatorGame): GSTCalculatorGame
    suspend fun abandonGame(gameId: Long)
    suspend fun getLevel(seed: Long, levelNumber: Int, difficulty: Difficulty): GSTCalculatorLevel
}

interface ChallengeRepository {
    suspend fun getChallenge(type: ChallengeType, key: String): ChallengeRecord?
    suspend fun createChallenge(type: ChallengeType, key: String, difficulty: Difficulty): ChallengeRecord
    suspend fun resolveActiveChallenge(type: ChallengeType): ChallengeRecord
    fun observeActiveChallenge(type: ChallengeType): Flow<ChallengeRecord?>
    suspend fun completeChallenge(record: ChallengeRecord, timeSeconds: Long, moves: Int): ChallengeRecord
    fun observeChallengeHistory(type: ChallengeType): Flow<List<ChallengeRecord>>
    suspend fun getCurrentStreak(type: ChallengeType): Int
    suspend fun getChallengeGame(record: ChallengeRecord): GSTCalculatorGame
}

interface ProgressionRepository {
    fun observeStats(): Flow<UserStats>
    suspend fun getStats(): UserStats
    suspend fun updateStatsAfterGame(game: GSTCalculatorGame)
    suspend fun grantChallengeRewards(rewardCoins: Int, rewardXp: Int)
    fun observePuzzleProfile(): Flow<PuzzleProfile>
    suspend fun getPuzzleProfile(): PuzzleProfile
    fun observeAchievements(): Flow<List<Achievement>>
    suspend fun checkAndUnlockAchievements(
        game: GSTCalculatorGame,
        sameDevicePlayed: Boolean = false
    ): List<Achievement>
    fun observeEconomy(): Flow<EconomyState>
    suspend fun getEconomy(): EconomyState
    suspend fun spendCoins(amount: Int): Boolean
    suspend fun earnCoins(amount: Int)
    suspend fun unlockTheme(themeId: String): Boolean
}

interface PreferencesRepository {
    fun getUserPreferences(): Flow<com.gstcalculator.domain.model.UserPreferences>
    suspend fun updatePreferences(transform: (com.gstcalculator.domain.model.UserPreferences) -> com.gstcalculator.domain.model.UserPreferences)
    suspend fun getCampaignLevel(difficulty: Difficulty): Int
    suspend fun advanceCampaignLevel(difficulty: Difficulty): Int
}
