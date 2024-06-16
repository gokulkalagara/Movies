package com.lloyds.media.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.usecase.FavouritesUseCase
import com.lloyds.media.domain.usecase.MediaUseCase
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.details.models.MediaDetailsAction
import com.lloyds.media.ui.components.details.models.MediaDetailsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val mediaUseCase: MediaUseCase,
    private val favouritesUseCase: FavouritesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val mediaId: Int
    private val mediaType: String

    private val _state = MutableStateFlow(MediaDetailsScreenUiState(true))
    val state: StateFlow<MediaDetailsScreenUiState> = _state

    init {
        mediaId = savedStateHandle["mediaId"] ?: -1
        mediaType = savedStateHandle["mediaType"] ?: "movie"
        getMediaDetails(mediaId, mediaType)
    }


    fun onAction(action: MediaDetailsAction) {
        when (action) {
            MediaDetailsAction.retry -> {
                _state.value = MediaDetailsScreenUiState(true)
                getMediaDetails(mediaId, mediaType)
            }

            is MediaDetailsAction.FavAction -> {
                actionOnFavourites(action.mediaDetailsModel, action.addFav)
            }
        }
    }

    private fun getMediaDetails(mediaId: Int, mediaType: String) {
        if (mediaId == 0) {
            MediaDetailsScreenUiState(
                isLoading = false,
                error = "There is no media id to obtain media details."
            )
            return
        }
        viewModelScope.launch {
            val isFav = when (val isFavWork = favouritesUseCase.isExistFavourites(mediaId)) {
                is Work.Result -> isFavWork.data
                else -> false
            }

            when (val work: Work<MediaDetailsModel> =
                mediaUseCase.getMediaDetails(mediaId, mediaType)) {
                is Work.Result -> {
                    _state.value = MediaDetailsScreenUiState(
                        isLoading = false, error = null, mediaDetailsModel = work.data, isFavourites = isFav
                    )
                }

                is Work.Stop -> {
                    _state.value =
                        MediaDetailsScreenUiState(isLoading = false, error = work.message.message)
                }

                is Work.Backfire -> {
                    _state.value =
                        MediaDetailsScreenUiState(isLoading = false, error = work.exception.message)
                }

                else -> {
                    _state.value = MediaDetailsScreenUiState(
                        isLoading = false, error = Constants.SOMETHING_WENT_WRONG
                    )
                }
            }
        }
    }

    private fun actionOnFavourites(mediaDetailsModel: MediaDetailsModel, addFav: Boolean) {
        viewModelScope.launch {
            if (addFav) {
                favouritesUseCase.addFavourites(mediaDetailsModel, createdAt = Date())
            } else {
                favouritesUseCase.deleteFavourites(mediaDetailsModel.id)
            }
        }
    }
}