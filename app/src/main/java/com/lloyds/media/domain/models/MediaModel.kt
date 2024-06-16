package com.lloyds.media.domain.models

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Parcelize
@Immutable
data class MediaModel(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val id: Int,

    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,

    val title: String?,
    @SerializedName("original_title")
    val originalTitle: String?,

    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("media_type")
    val mediaType: String,
    val adult: Boolean?,

    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    val popularity: Double?,

    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("origin_country")
    val originCountry: List<String>?
) : Parcelable

fun MediaModel.getIntroDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)

    val dateString = if (mediaType.equals("movie")) {
        releaseDate ?: ""
    } else {
        firstAirDate ?: ""
    }

    if (dateString.isEmpty()) {
        return "Unavailable"
    }
    val date: Date? = inputFormat.parse(dateString)
    return date?.let { outputFormat.format(it) } ?: "Unavailable"
}

fun MediaModel.getExactTitle(): String {
    return originalName ?: name ?: originalTitle ?: title ?: ""
}

