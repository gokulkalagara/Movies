package com.lloyds.media.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.usecase.TrendingUseCase
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.home.models.Pagination
import com.lloyds.media.ui.components.home.models.TrendingAction
import com.lloyds.media.ui.components.home.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@HiltViewModel
class TrendingViewModel @Inject constructor(private val trendingUseCase: TrendingUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(UiState<MutableList<MediaModel>>(isLoading = true))
    val state: StateFlow<UiState<MutableList<MediaModel>>> = _state
    private var trendingJob: Job? = null

    init {
        getTrendingMedia()
    }

    fun onAction(action: TrendingAction) {
        when (action) {
            TrendingAction.retry -> {
                _state.value = UiState(isLoading = true)
                getTrendingMedia()
            }

            TrendingAction.loadMore -> {
                getTrendingMedia()
            }

            else -> {}
        }
    }

    private fun getTrendingMedia() {
        if (isNotAllowedToFetch()) {
            return
        }

        trendingJob = viewModelScope.launch {
            when (val work: Work<List<MediaModel>> = trendingUseCase.getTrendingMedia()) {
                is Work.Result -> {
                    val pagination =
                        if (trendingUseCase.isPaginationCompleted()) Pagination.DONE
                        else Pagination.LOADING
                    val mutableList : MutableList<MediaModel> = mutableListOf()
                    mutableList.addAll(work.data)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = null,
                        data = mutableList,
                        pagination = pagination
                    )
                }

                is Work.Stop -> {
                    _state.value =
                        UiState(isLoading = false, error = work.message.message)
                }

                is Work.Backfire -> {
                    _state.value =
                        UiState(isLoading = false, error = work.exception.message)
                }

                else -> {
                    _state.value = UiState(
                        isLoading = false,
                        error = Constants.SOMETHING_WENT_WRONG
                    )
                }
            }
        }
    }

    private fun isNotAllowedToFetch(): Boolean {
        return trendingJob?.isActive == true || trendingUseCase.isPaginationCompleted()
    }

}