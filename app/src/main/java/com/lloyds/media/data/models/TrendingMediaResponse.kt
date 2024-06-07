package com.lloyds.media.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lloyds.media.domain.models.MediaModel
import kotlinx.parcelize.Parcelize

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Parcelize
data class TrendingMediaResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: List<MediaModel>,
    @SerializedName("total_results")
    val totalResults: Int
) : Parcelable
