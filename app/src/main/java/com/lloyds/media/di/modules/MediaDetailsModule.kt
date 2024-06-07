package com.lloyds.media.di.modules

import com.lloyds.media.data.repository.MediaRepositoryImpl
import com.lloyds.media.data.repository.datasource.MediaRemoteDateSource
import com.lloyds.media.domain.repository.MediaRepository
import com.lloyds.media.domain.usecase.MediaUseCase
import com.lloyds.media.domain.usecase.impl.MediaUseCaseImpl
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
object MediaDetailsModule {
    @Provides
    @Singleton
    fun provideMediaDetailsRemoteDataSource(retrofit: Retrofit): MediaRemoteDateSource {
        return MediaRemoteDateSource(retrofit)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaDetailsBindModule {
    @Binds
    @Singleton
    abstract fun bindsMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindsMediaUseCase(mediaUseCaseImpl: MediaUseCaseImpl): MediaUseCase
}