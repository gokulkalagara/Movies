package com.lloyds.media.domain.usecase.impl

import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.repository.MediaRepository
import com.lloyds.media.domain.usecase.MediaUseCase
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import com.lloyds.media.ui.viewmodels.models.Message
import com.lloyds.media.ui.viewmodels.models.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaUseCaseImpl @Inject constructor(private val repository: MediaRepository) : MediaUseCase {
    override suspend fun getMediaDetails(mediaId: Int, mediaType: String): Work<MediaDetailsModel> {
        val result = withContext(Dispatchers.IO) {
            repository.getMediaDetails(mediaId, mediaType)
        }
        return handleResponse(mediaId, result)
    }

    private fun handleResponse(
        id: Int, result: NetworkResult<MediaDetailsModel?, FailedResponse, Exception>
    ): Work<MediaDetailsModel> {
        when (result) {
            is NetworkResult.Success -> {
                val mediaDetailsModel = result.data
                return mediaDetailsModel?.let {
                    return Work.result(mediaDetailsModel)
                } ?: Work.Stop(message = Message(message = "Data not available for this id : $id"))
            }

            is NetworkResult.Failed -> {
                return Work.stop(
                    message = Message(
                        message = "unable to fetch the media details for this id : $id",
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