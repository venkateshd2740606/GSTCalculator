package com.gstcalculator.di

import android.content.Context
import androidx.room.Room
import com.gstcalculator.data.local.database.GSTCalculatorDatabase
import com.gstcalculator.data.local.database.dao.AchievementDao
import com.gstcalculator.data.local.database.dao.ChallengeDao
import com.gstcalculator.data.local.database.dao.EconomyDao
import com.gstcalculator.data.local.database.dao.GameDao
import com.gstcalculator.data.local.database.dao.ProfileDao
import com.gstcalculator.data.local.database.dao.StatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GSTCalculatorDatabase =
        Room.databaseBuilder(context, GSTCalculatorDatabase::class.java, "GSTCalculator.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideGameDao(db: GSTCalculatorDatabase): GameDao = db.gameDao()
    @Provides fun provideStatsDao(db: GSTCalculatorDatabase): StatsDao = db.statsDao()
    @Provides fun provideAchievementDao(db: GSTCalculatorDatabase): AchievementDao = db.achievementDao()
    @Provides fun provideChallengeDao(db: GSTCalculatorDatabase): ChallengeDao = db.challengeDao()
    @Provides fun provideEconomyDao(db: GSTCalculatorDatabase): EconomyDao = db.economyDao()
    @Provides fun provideProfileDao(db: GSTCalculatorDatabase): ProfileDao = db.profileDao()
}
