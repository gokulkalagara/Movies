package com.lloyds.media.domain.usecase

import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.Work

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface TrendingUseCase {
    suspend fun getTrendingMedia(): Work<List<MediaModel>>
}