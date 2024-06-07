package com.lloyds.media.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Parcelize
data class TVShowDetailsModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,

    @SerializedName("created_by")
    val createdBy: List<Creator>?,
    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>?,

    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("in_production")
    val inProduction: Boolean?,
    @SerializedName("languages")
    val languages: List<String>?,
    @SerializedName("last_air_date")
    val lastAirDate: String?,
    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: EpisodeDetails?,
    @SerializedName("next_episode_to_air")
    val nextEpisodeToAir: EpisodeDetails?,
    @SerializedName("networks")
    val networks: List<Network>?,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("type")
    val type: String?
) : MediaDetailsModel(
    adult = false,
    backdropPath = "",
    genres = listOf(),
    homepage = "",
    id = -1,
    originCountry = listOf(),
    originalLanguage = "",
    overview = "",
    popularity = -1.0,
    posterPath = "",
    productionCompanies = listOf(),
    productionCountries = listOf(),
    spokenLanguages = listOf(),
    tagline = "",
    voteAverage = -1.0,
    voteCount = -1
), Parcelable

@Parcelize
data class Creator(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("credit_id")
    val creditId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("profile_path")
    val profilePath: String?
) : Parcelable

@Parcelize
data class EpisodeDetails(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    @SerializedName("episode_type")
    val episodeType: String?,
    @SerializedName("production_code")
    val productionCode: String?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("show_id")
    val showId: Int?,
    @SerializedName("still_path")
    val stillPath: String?
) : Parcelable

@Parcelize
data class Network(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("origin_country")
    val originCountry: String?
) : Parcelable

@Parcelize
data class Season(
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("episode_count")
    val episodeCount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("vote_average")
    val voteAverage: Double?
) : Parcelable