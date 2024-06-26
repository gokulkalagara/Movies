package com.lloyds.media.domain.usecase.impl

import com.lloyds.media.data.models.TrendingMediaResponse
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.repository.TrendingRepository
import com.lloyds.media.domain.usecase.TrendingUseCase
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import com.lloyds.media.ui.viewmodels.models.Message
import com.lloyds.media.ui.viewmodels.models.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrendingUseCaseImpl @Inject constructor(private val repository: TrendingRepository) :
    TrendingUseCase {
    private var page: Int = 1
    private var pagination: Boolean = false
    private val models = mutableListOf<MediaModel>()

    override suspend fun getTrendingMedia(): Work<List<MediaModel>> {
        val result = withContext(Dispatchers.IO) {
            repository.getTrendingMedia(page)
        }
        return handleResponse(result)
    }

    override fun isPaginationCompleted(): Boolean {
        return pagination
    }

    private fun handleResponse(result: NetworkResult<TrendingMediaResponse?, FailedResponse, Exception>): Work<List<MediaModel>> {
        when (result) {
            is NetworkResult.Success -> {
                val trendingMovieResponse = result.data
                trendingMovieResponse?.let {
                    models.addAll(it.results)
                }
                page += 1
                if (page == 20) {
                    pagination = true
                }
                return Work.result(models)
            }

            is NetworkResult.Failed -> {
                return Work.stop(
                    message = Message(
                        message = "unable to fetch trending media, page : ${page}",
                        messageType = MessageType.ALERT
                    )
                )
            }

            is NetworkResult.Error -> {
                return Work.backfire(
                    exception = result.exception
                )
            }

            else -> {
                return Work.backfire(
                    exception = RuntimeException(Constants.SOMETHING_WENT_WRONG)
                )
            }
        }
    }
}