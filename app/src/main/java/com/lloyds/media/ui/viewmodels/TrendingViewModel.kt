package com.lloyds.media.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.usecase.TrendingUseCase
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.home.models.TrendingAction
import com.lloyds.media.ui.components.home.models.TrendingScreenUiState
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
    private val _state = MutableStateFlow(TrendingScreenUiState(true))
    val state: StateFlow<TrendingScreenUiState> = _state
    private var trendingJob: Job? = null


    init {
        getTrendingMedia()
    }

    fun onAction(action: TrendingAction) {
        when (action) {
            TrendingAction.retry -> {
                _state.value = TrendingScreenUiState(true)
                getTrendingMedia()
            }

            else -> {}
        }
    }

    private fun getTrendingMedia() {
        if (!isAllowedToFetch()) {
            return
        }

        trendingJob = viewModelScope.launch {
            when (val work: Work<List<MediaModel>> = trendingUseCase.getTrendingMedia()) {
                is Work.Result -> {
                    _state.value =
                        TrendingScreenUiState(isLoading = false, error = null, list = work.data)
                }

                is Work.Stop -> {
                    _state.value =
                        TrendingScreenUiState(isLoading = false, error = work.message.message)
                }

                is Work.Backfire -> {
                    _state.value =
                        TrendingScreenUiState(isLoading = false, error = work.exception.message)
                }

                else -> {
                    _state.value = TrendingScreenUiState(
                        isLoading = false,
                        error = Constants.SOMETHING_WENT_WRONG
                    )
                }
            }
        }
    }

    private fun isAllowedToFetch(): Boolean {
        return trendingJob?.isActive != true
    }

}