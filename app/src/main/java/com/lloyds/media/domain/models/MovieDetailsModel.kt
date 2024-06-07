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
data class MovieDetailsModel(
    @SerializedName("title")
    val title: String?,
    @SerializedName("original_title")
    val originalTitle: String?,

    @SerializedName("belongs_to_collection")
    val belongsToCollection: CollectionDetails?,

    @SerializedName("budget")
    val budget: Int?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("revenue")
    val revenue: Long?,

    @SerializedName("runtime")
    val runtime: Int?,

    @SerializedName("video")
    val video: Boolean?
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
data class CollectionDetails(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?
) : Parcelable

@Parcelize
data class Genre(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
) : Parcelable

@Parcelize
data class ProductionCompany(
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
data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso3166C1: String?,
    @SerializedName("name")
    val name: String?
) : Parcelable

@Parcelize
data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String?,
    @SerializedName("iso_639_1")
    val iso639_1: String?,
    @SerializedName("name")
    val name: String?
) : Parcelable