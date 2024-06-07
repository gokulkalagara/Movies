package com.lloyds.media.di.modules

import com.lloyds.media.data.repository.FavouritesRepositoryImpl
import com.lloyds.media.data.repository.datasource.FavouritesLocalDataSource
import com.lloyds.media.domain.repository.FavouritesRepository
import com.lloyds.media.domain.usecase.FavouritesUseCase
import com.lloyds.media.domain.usecase.impl.FavouritesUseCaseImpl
import com.lloyds.media.infra.database.MediaDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object FavouritesModule {

    @Provides
    @Singleton
    fun provideFavouritesMediaDataSource(database: MediaDatabase): FavouritesLocalDataSource {
        return FavouritesLocalDataSource(database)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class FavouritesBindModule {
    @Binds
    @Singleton
    abstract fun bindsFavouritesRepository(favouritesRepositoryImpl: FavouritesRepositoryImpl): FavouritesRepository

    @Binds
    @Singleton
    abstract fun bindsFavouritesUseCase(favouritesUseCaseImpl: FavouritesUseCaseImpl): FavouritesUseCase
}