package com.lloyds.media.domain.repository

import com.lloyds.media.data.models.TrendingMediaResponse
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface TrendingRepository {
    suspend fun getTrendingMedia(page: Int): NetworkResult<TrendingMediaResponse?, FailedResponse, Exception>
}