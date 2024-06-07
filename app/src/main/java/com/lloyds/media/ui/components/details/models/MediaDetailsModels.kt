package com.lloyds.media.ui.components.details.models

import com.lloyds.media.domain.models.MediaDetailsModel

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
data class MediaDetailsScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mediaDetailsModel: MediaDetailsModel? = null,
    val isFavourites: Boolean = false
)

sealed class MediaDetailsAction {
    object retry : MediaDetailsAction()
    data class FavAction(val mediaDetailsModel: MediaDetailsModel, val addFav: Boolean = false) : MediaDetailsAction()
}