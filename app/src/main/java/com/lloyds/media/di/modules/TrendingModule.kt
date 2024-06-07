package com.lloyds.media.di.modules

import com.lloyds.media.data.repository.TrendingRepositoryImpl
import com.lloyds.media.data.repository.datasource.TrendingRemoteDateSource
import com.lloyds.media.domain.repository.TrendingRepository
import com.lloyds.media.domain.usecase.TrendingUseCase
import com.lloyds.media.domain.usecase.impl.TrendingUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object TrendingModule {
    @Provides
    @Singleton
    fun provideTrendingRemoteDataSource(retrofit: Retrofit): TrendingRemoteDateSource {
        return TrendingRemoteDateSource(retrofit)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TrendingBindModule {
    @Binds
    @Singleton
    abstract fun bindsTrendingRepository(trendingRepositoryImpl: TrendingRepositoryImpl): TrendingRepository

    @Binds
    @Singleton
    abstract fun bindsTrendingUseCase(trendingUseCaseImpl: TrendingUseCaseImpl): TrendingUseCase
}
