package com.lloyds.media.data.repository

import com.lloyds.media.data.models.TrendingMediaResponse
import com.lloyds.media.data.repository.datasource.TrendingRemoteDateSource
import com.lloyds.media.domain.repository.TrendingRepository
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(private val remoteDataSource: TrendingRemoteDateSource) :
    TrendingRepository {
    override suspend fun getTrendingMedia(page: Int): NetworkResult<TrendingMediaResponse?, FailedResponse, Exception> {
        return remoteDataSource.getTrendingMedia(page, Constants.LANGUAGE, Constants.API_KEY)
    }
}