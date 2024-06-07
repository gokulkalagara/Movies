package com.lloyds.media.ui.components.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lloyds.media.R
import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.ui.components.ErrorUICompose
import com.lloyds.media.ui.components.ProgressBarCompose
import com.lloyds.media.ui.components.details.models.MediaDetailsAction
import com.lloyds.media.ui.components.details.models.MediaDetailsScreenUiState
import com.lloyds.media.ui.viewmodels.MediaDetailsViewModel
import com.lloyds.media.utils.FontUtils
import com.lloyds.media.utils.ImageDim
import com.lloyds.media.utils.UrlUtils


/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 * copyright (c) 2024, All rights reserved.
 *
 */
@Composable
fun MediaDetailsScreen(viewModel: MediaDetailsViewModel = hiltViewModel()) {
    val uiState = remember {
        mutableStateOf(MediaDetailsScreenUiState())
    }

    val favouriteState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel) {
        viewModel.state.collect {
            uiState.value = it
            favouriteState.value = it.isFavourites
        }
    }

    MediaDetailsScreenContent(uiState.value, favouriteState.value) {
        if (it is MediaDetailsAction.FavAction) {
            favouriteState.value = it.addFav
        }
        viewModel.onAction(it)
    }
}

@Composable
fun MediaDetailsScreenContent(
    uiState: MediaDetailsScreenUiState, isFavourite: Boolean, action: (MediaDetailsAction) -> Unit
) {
    if (uiState.isLoading) {
        ProgressBarCompose()
    } else if ((uiState.error?.length ?: 0) > 0) {
        ErrorUICompose(uiState.error ?: "", callBack = {
            action(MediaDetailsAction.retry)
        })
    } else {

        uiState.mediaDetailsModel?.let {
            MediaDetails(it, isFavourite, action)
        }
    }
}

@Composable
fun MediaDetails(
    model: MediaDetailsModel,
    isFavourite: Boolean,
    action: (MediaDetailsAction) -> Unit
) {

    LazyColumn {
        item {
            TopBannerDetails(model, isFavourite, action)
        }
        item {
            ItemTitleInfo(model)
        }
    }
}


@Composable
fun TopBannerDetails(
    model: MediaDetailsModel,
    isFavourite: Boolean,
    action: (MediaDetailsAction) -> Unit
) {

    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(UrlUtils.getImageUrl(model.backdropPath, ImageDim.FULL)).build(),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = colorResource(id = R.color.light_tint_gray))
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(UrlUtils.getImageUrl(model.posterPath, ImageDim.FULL)).build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 130.dp, start = 16.dp)
                .background(
                    color = colorResource(id = R.color.white), shape = CircleShape
                )
                .padding(5.dp)
                .background(color = Color.LightGray, shape = CircleShape)
                .padding(2.dp)
                .size(120.dp)
                .align(Alignment.BottomStart)
                .clip(shape = CircleShape)
        )


        CircularProgressIndicator(
            progress = { (model.voteAverage?.toFloat()?.div(10)) ?: 0f },
            modifier = Modifier
                .padding(top = 129.dp, start = 16.dp)
                .align(Alignment.BottomStart)
                .size(134.dp),
            color = colorResource(id = R.color.purple_200),
        )

        val percentage = ((model.voteAverage?.toFloat()?.div(10))?.times(100))?.toInt()
        Text(
            text = buildAnnotatedString {
                append("$percentage")
                withStyle(style = SpanStyle(fontSize = 8.sp)) {
                    append(
                        "%"
                    )
                }
            },
            letterSpacing = TextUnit(1F, TextUnitType.Sp),
            fontFamily = FontUtils.robotoFamily,
            fontWeight = FontWeight.Thin,
            color = colorResource(id = R.color.icon_black),
            fontSize = 16.sp,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 152.dp, end = 16.dp, bottom = 28.dp)
        )


        Image(
            painter = painterResource(id = if (isFavourite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border),
            contentDescription = "",
            colorFilter = ColorFilter.tint(color = Color.Red),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable {
                    action(MediaDetailsAction.FavAction(model, !isFavourite))
                }
                .padding(end = 16.dp)
                .size(48.dp)
                .padding(5.dp)
        )
    }
}

@Composable
fun ItemTitleInfo(model: MediaDetailsModel) {

    LazyRow(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp), content = {
        model.genres?.forEach {
            item {
                Text(
                    text = if (it == model.genres.last()) it.name.toString()
                        .uppercase() else it.name?.uppercase() + " • ",
                    color = colorResource(id = R.color.icon_black),
                    letterSpacing = TextUnit(2F, TextUnitType.Sp),
                    fontFamily = FontUtils.robotoFamily,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
    })

    Text(
        text = "Overview",
        fontSize = 20.sp,
        fontFamily = FontUtils.robotoFamily,
        fontWeight = FontWeight(600),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = colorResource(id = R.color.icon_black),
        letterSpacing = TextUnit(0.5F, TextUnitType.Sp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

    Text(
        text = model.overview ?: "",
        fontSize = 16.sp,
        fontFamily = FontUtils.robotoFamily,
        fontWeight = FontWeight.Normal,
        overflow = TextOverflow.Ellipsis,
        lineHeight = TextUnit(24F, TextUnitType.Sp),
        letterSpacing = TextUnit(0.7F, TextUnitType.Sp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    )

}



