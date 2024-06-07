package com.lloyds.media.di.modules

import android.content.Context
import androidx.room.Room
import com.lloyds.media.infra.database.MediaDatabase
import com.lloyds.media.infra.local.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun getAppDatabase(@ApplicationContext context: Context): MediaDatabase {
        return Room.databaseBuilder(context, MediaDatabase::class.java, Constants.DB.NAME).build()
    }
}