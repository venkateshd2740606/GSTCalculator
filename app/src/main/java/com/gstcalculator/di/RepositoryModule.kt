package com.gstcalculator.di

import com.gstcalculator.data.repository.ChallengeRepositoryImpl
import com.gstcalculator.data.repository.GameRepositoryImpl
import com.gstcalculator.data.repository.PreferencesRepositoryImpl
import com.gstcalculator.data.repository.ProgressionRepositoryImpl
import com.gstcalculator.domain.repository.ChallengeRepository
import com.gstcalculator.domain.repository.GameRepository
import com.gstcalculator.domain.repository.PreferencesRepository
import com.gstcalculator.domain.repository.ProgressionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository
    @Binds @Singleton abstract fun bindChallengeRepository(impl: ChallengeRepositoryImpl): ChallengeRepository
    @Binds @Singleton abstract fun bindProgressionRepository(impl: ProgressionRepositoryImpl): ProgressionRepository
    @Binds @Singleton abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
