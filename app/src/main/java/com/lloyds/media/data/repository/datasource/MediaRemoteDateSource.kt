package com.lloyds.media.data.repository.datasource

import com.lloyds.media.data.repository.datasource.base.BaseRemoteDataSource
import com.lloyds.media.data.repository.service.IMediaService
import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
class MediaRemoteDateSource @Inject constructor(private val retrofit: Retrofit) :
    BaseRemoteDataSource() {

    private val mediaService: IMediaService by lazy { retrofit.create(IMediaService::class.java) }

    suspend fun getMediaDetails(
        mediaId: Int,
        mediaType: String,
        language: String,
        apiKey: String,
    ): NetworkResult<MediaDetailsModel?, FailedResponse, Exception> {
        return performSafeApiRequestCall {
            if (mediaType.equals("tv")) {
                mediaService.getTvMediaDetails(mediaId, mediaType, language, apiKey)
            } else {
                mediaService.getMovieMediaDetails(mediaId, mediaType, language, apiKey)
            }
        }
    }
}