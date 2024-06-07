package com.lloyds.media.infra.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import java.util.Optional

/**
 * @Author: Gokul Kalagara
 * copyright (c) 2024, All rights reserved.
 *
 */


@Dao
interface MediaFavouritesDao {
    @Transaction
    @Insert
    suspend fun insert(entity: MediaFavouritesEntity)

    @Query("SELECT * FROM media_favourites WHERE id=:id")
    suspend fun findById(id: Int): Optional<MediaFavouritesEntity>

    @Query("DELETE FROM media_favourites WHERE id=:id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * from media_favourites ORDER BY createdAt DESC")
    suspend fun findAll(): List<MediaFavouritesEntity>
}