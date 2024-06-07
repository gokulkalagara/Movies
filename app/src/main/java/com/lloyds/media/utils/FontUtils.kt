package com.lloyds.media.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.lloyds.media.R

/**
 * @Author: Gokul Kalagara
 * copyright (c) 2024, All rights reserved.
 *
 */
object FontUtils {
    val robotoFamily = FontFamily(
        Font(R.font.roboto_thin, FontWeight.Thin),
        Font(R.font.roboto_thinitalic, FontWeight.Thin, FontStyle.Italic),
        Font(R.font.roboto_light, FontWeight.Light),
        Font(R.font.roboto_lightitalic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.roboto_regular, FontWeight.Normal),
        Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.roboto_medium, FontWeight.Medium),
        Font(R.font.roboto_mediumitalic, FontWeight.Medium, FontStyle.Italic),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_bolditalic, FontWeight.Medium, FontStyle.Italic)
    )
}