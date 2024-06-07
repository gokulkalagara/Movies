package com.lloyds.media.utils

import com.lloyds.media.infra.local.Constants


/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
object UrlUtils {
    fun getImageUrl(path: String?, dim: ImageDim): String {
        return when (dim) {
            ImageDim.W200 -> Constants.Apis.IMAGE_W200_BASE_URL.plus(path)
            ImageDim.W500 -> Constants.Apis.IMAGE_W500_BASE_URL.plus(path)
            ImageDim.FULL -> Constants.Apis.ORIGINAL_IMAGE_URL.plus(path)
        }
    }

}

enum class ImageDim {
    W200, W500, FULL
}