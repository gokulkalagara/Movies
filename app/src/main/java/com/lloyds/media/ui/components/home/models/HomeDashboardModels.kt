package com.lloyds.media.ui.components.home.models

import com.lloyds.media.R
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
        "home://details/{mediaType}/{mediaId}?mediaTitle={mediaTitle}",
        R.drawable.ic_movie_filter_24,
        "Media Details"
    ) {
        fun route(arg1: String, arg2: Int, arg3: String) = "home://details/$arg1/$arg2?mediaTitle=$arg3"
    }
}

data class UiState<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null,
    val pagination: Pagination? = null
)

sealed class TrendingAction {
    object retry : TrendingAction()
    object loadMore : TrendingAction()
    data class MediaDetailsNavigate(
        val mediaId: Int,
        val mediaType: String,
        val mediaTitle: String,
    ) : TrendingAction()
}

data class FavouritesScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val list: MutableList<MediaFavouritesEntity> = mutableListOf()
)

sealed class FavouritesAction {
    object retry : FavouritesAction()
    data class FavItemClick(val entity: MediaFavouritesEntity) : FavouritesAction()
}

enum class Pagination {
    NONE,
    LOAD_MORE,
    LOADING,
    ERROR_RETRY,
    DONE
}