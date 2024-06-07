package com.lloyds.media.domain.usecase

import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.Work

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface MediaUseCase {
    suspend fun getMediaDetails(mediaId: Int, mediaType: String): Work<MediaDetailsModel>
}