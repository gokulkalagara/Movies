package com.lloyds.media.data.repository.service

import com.lloyds.media.domain.models.MovieDetailsModel
import com.lloyds.media.domain.models.TVShowDetailsModel
import com.lloyds.media.infra.local.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
interface IMediaService {
    @GET(Constants.Apis.API_GET_MEDIA_DETAILS_URL)
    suspend fun getMovieMediaDetails(
        @Path("id") mediaId: Int,
        @Path("mediaType") mediaType: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): Response<MovieDetailsModel>

    @GET(Constants.Apis.API_GET_MEDIA_DETAILS_URL)
    suspend fun getTvMediaDetails(
        @Path("id") mediaId: Int,
        @Path("mediaType") mediaType: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): Response<TVShowDetailsModel>

}