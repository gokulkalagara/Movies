package com.lloyds.media.ui.components.home.models

import com.lloyds.media.R
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity

/**
 * @Author: Gokul Kalagara
 * copyright (c) 2024, All rights reserved.
 *
 */

sealed class HomeScreen(val route: String, val drawable: Int, val title: String) {
    data object Trending : HomeScreen("home://trending", R.drawable.ic_trending, "Trending")
    data object Favourites :
        HomeScreen("home://favourites", R.drawable.ic_favorite_24, "Favourites")

    data object MediaDetails : HomeScreen(
        "home://details/{mediaId}/{mediaType}/{mediaTitle}",
        R.drawable.ic_movie_filter_24,
        "Media Details"
    ) {
        fun route(arg1: Int, arg2: String, arg3: String) = "home://details/$arg1/$arg2/$arg3"
    }
}

data class TrendingScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val list: List<MediaModel> = emptyList()
)

sealed class TrendingAction {
    object retry : TrendingAction()
    data class MediaDetailsNavigate(
        val mediaId: Int,
        val mediaType: String,
        val mediaTitle: String,
    ) : TrendingAction()
}

data class FavouritesScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val list: List<MediaFavouritesEntity> = emptyList()
)

sealed class FavouritesAction {
    object retry : FavouritesAction()
}