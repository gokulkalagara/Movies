package com.lloyds.media.domain.usecase

import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import java.util.Date

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface FavouritesUseCase {
    suspend fun addFavourites(mediaItem: MediaDetailsModel, createdAt : Date): Work<MediaFavouritesEntity>
    suspend fun isExistFavourites(mediaId: Int): Work<Boolean>
    suspend fun deleteFavourites(mediaId: Int): Work<Boolean>
    suspend fun getAllFavourites(): Work<List<MediaFavouritesEntity>>
}