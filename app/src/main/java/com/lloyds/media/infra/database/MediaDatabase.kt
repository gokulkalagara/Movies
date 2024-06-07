package com.lloyds.media.infra.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lloyds.media.infra.database.converter.DateConverter
import com.lloyds.media.infra.database.dao.MediaFavouritesDao
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity

/**
 * @Author: Gokul Kalagara
 *
 */
@Database(entities = [MediaFavouritesEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun getMediaFavouritesDao(): MediaFavouritesDao
}