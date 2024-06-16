package com.lloyds.media.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.usecase.FavouritesUseCase
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.home.models.FavouritesAction
import com.lloyds.media.ui.components.home.models.FavouritesScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
class FavouritesViewModel @Inject constructor(private val favouritesUseCase: FavouritesUseCase) : ViewModel() {
    private val _state = MutableStateFlow(FavouritesScreenUiState(true))
    val state: StateFlow<FavouritesScreenUiState> = _state

    init {
        getAllMediaFavourites()
    }

    private fun getAllMediaFavourites() {
        viewModelScope.launch {
            when (val work: Work<List<MediaFavouritesEntity>> =
                favouritesUseCase.getAllFavourites()) {
                is Work.Result -> {
                    val mutableList = mutableListOf<MediaFavouritesEntity>()
                    mutableList.addAll(work.data)
                    _state.value =
                        FavouritesScreenUiState(isLoading = false, error = null, list = mutableList)
                }

                is Work.Backfire -> {
                    _state.value =
                        FavouritesScreenUiState(isLoading = false, error = work.exception.message)
                }

                else -> {
                    _state.value = FavouritesScreenUiState(
                        isLoading = false,
                        error = Constants.SOMETHING_WENT_WRONG
                    )
                }
            }
        }
    }

    fun onAction(action: FavouritesAction) {
        when (action) {
            FavouritesAction.retry -> {
                _state.value = FavouritesScreenUiState(true)
                getAllMediaFavourites()
            }

            else -> {}
        }
    }

}