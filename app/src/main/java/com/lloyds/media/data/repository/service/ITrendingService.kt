package com.lloyds.media.data.repository.service

import com.lloyds.media.data.models.TrendingMediaResponse
import com.lloyds.media.infra.local.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface ITrendingService {
    @GET(Constants.Apis.API_GET_TRENDING_MEDIA_URL)
    suspend fun getTrendingMedia(
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): Response<TrendingMediaResponse>
}