package com.lloyds.media.infra.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * @Author: Gokul Kalagara
 * copyright (c) 2024, All rights reserved.
 *
 */


@Entity(tableName = "media_favourites")
data class MediaFavouritesEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val mediaType: String? = null,
    val overview: String? = null,
    val imageUrl: String? = null,
    @ColumnInfo(name = "createdAt") val createdAt: Date = Date(System.currentTimeMillis())
)