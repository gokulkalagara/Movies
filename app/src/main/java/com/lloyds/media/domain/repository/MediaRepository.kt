package com.lloyds.media.domain.repository

import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface MediaRepository {
    suspend fun getMediaDetails(
        mediaId: Int,
        mediaType: String
    ): NetworkResult<MediaDetailsModel?, FailedResponse, Exception>
}