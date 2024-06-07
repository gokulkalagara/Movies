package com.lloyds.media.infra.local

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 *
 */
object Constants {
    const val API_KEY: String = "22012045e87f46091838788e961af343"
    const val LANGUAGE: String = "en-US"
    const val TIME_OUT: Long = 2
    const val SOMETHING_WENT_WRONG: String = "Something went wrong"

    object DB {
        const val NAME: String = "MEDIA_DB"
    }

    object Apis {
        const val BASE_URL: String = "https://api.themoviedb.org/3/"
        const val API_GET_TRENDING_MEDIA_URL: String = "trending/all/day"
        const val API_GET_MEDIA_DETAILS_URL: String = "{mediaType}/{id}"


        const val IMAGE_W200_BASE_URL: String = "https://image.tmdb.org/t/p/w200"
        const val IMAGE_W500_BASE_URL: String = "https://image.tmdb.org/t/p/w500"
        const val ORIGINAL_IMAGE_URL: String = "https://image.tmdb.org/t/p/original"
    }

}

