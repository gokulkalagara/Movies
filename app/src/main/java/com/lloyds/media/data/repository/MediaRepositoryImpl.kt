package com.lloyds.media.data.repository

import com.lloyds.media.data.repository.datasource.MediaRemoteDateSource
import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.repository.MediaRepository
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(private val remoteDateSource: MediaRemoteDateSource) :
    MediaRepository {
    override suspend fun getMediaDetails(
        mediaId: Int,
        mediaType: String
    ): NetworkResult<MediaDetailsModel?, FailedResponse, Exception> {
        return remoteDateSource.getMediaDetails(
            mediaId,
            mediaType,
            Constants.LANGUAGE,
            Constants.API_KEY
        )
    }
}