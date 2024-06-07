package com.lloyds.media.data.repository.datasource

import com.lloyds.media.data.models.TrendingMediaResponse
import com.lloyds.media.data.repository.datasource.base.BaseRemoteDataSource
import com.lloyds.media.data.repository.service.ITrendingService
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
class TrendingRemoteDateSource @Inject constructor(private val retrofit: Retrofit) :
    BaseRemoteDataSource() {

    private val trendingService: ITrendingService by lazy { retrofit.create(ITrendingService::class.java) }

    suspend fun getTrendingMedia(
        page: Int,
        language: String,
        apiKey: String,
    ): NetworkResult<TrendingMediaResponse?, FailedResponse, Exception> {
        return performSafeApiRequestCall {
            trendingService.getTrendingMedia(page, language, apiKey)
        }
    }
}