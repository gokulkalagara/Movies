package com.lloyds.media.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lloyds.media.R
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.getExactTitle
import com.lloyds.media.domain.models.getIntroDate
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.ErrorUICompose
import com.lloyds.media.ui.components.ProgressBarCompose
import com.lloyds.media.ui.components.home.models.TrendingAction
import com.lloyds.media.ui.components.home.models.TrendingScreenUiState
import com.lloyds.media.ui.viewmodels.TrendingViewModel
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
fun TrendingScreen(
    viewModel: TrendingViewModel = hiltViewModel(),
    navigate: (Int, String, String) -> Unit
) {
    val uiState = remember {
        mutableStateOf(TrendingScreenUiState())
    }
    LaunchedEffect(viewModel) {
        viewModel.state.collect {
            uiState.value = it
        }
    }

    TrendingScreenContent(uiState.value) {
        when (it) {
            is TrendingAction.MediaDetailsNavigate -> {
                navigate(it.mediaId, it.mediaType, it.mediaTitle)
            }

            else -> {
                viewModel.onAction(it)
            }
        }
    }
}

@Composable
fun TrendingScreenContent(uiState: TrendingScreenUiState, action: (TrendingAction) -> Unit) {
    if (uiState.isLoading) {
        ProgressBarCompose()
    } else if ((uiState.error?.length ?: 0) > 0) {
        ErrorUICompose(uiState.error ?: "", callBack = {
            action(TrendingAction.retry)
        })
    } else {
        TrendingMediaList(uiState.list, action = action)
    }
}

@Composable
fun TrendingMediaList(list: List<MediaModel>, action: (TrendingAction) -> Unit) {
    LazyColumn {
        items(list, key = { item -> item.id }) {
            TrendingMediaItem(it, action)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TrendingMediaItem(media: MediaModel, action: (TrendingAction) -> Unit) {
    Surface(
        shadowElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 1.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(3.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    action(
                        TrendingAction.MediaDetailsNavigate(
                            media.id,
                            media.mediaType,
                            media.getExactTitle()
                        )
                    )
                }
        ) {
            val (box1, text1, textDate, textDesc) = createRefs()
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .constrainAs(box1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .height(130.dp)
                .background(color = colorResource(id = R.color.light_tint_gray))) {
                AsyncImage(
                    model = UrlUtils.getImageUrl(media.posterPath, ImageDim.W200),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(90.dp)
                )
            }

            Text(text = media.getExactTitle(),
                fontSize = 16.sp,
                fontFamily = FontUtils.robotoFamily,
                fontWeight = FontWeight(600),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = TextUnit(0.5F, TextUnitType.Sp),
                modifier = Modifier.constrainAs(text1) {
                    start.linkTo(box1.end, margin = 16.dp)
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                })

            Text(text = media.getIntroDate().uppercase(),
                color = colorResource(id = R.color.text_color),
                fontSize = 10.sp,
                fontFamily = FontUtils.robotoFamily,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                letterSpacing = TextUnit(0.5F, TextUnitType.Sp),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(textDate) {
                    start.linkTo(box1.end, margin = 16.dp)
                    top.linkTo(text1.bottom, margin = 5.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = Dimension.fillToConstraints
                })

            Text(text = media.overview ?: "",
                color = colorResource(id = R.color.light_gray),
                fontSize = 12.sp,
                fontFamily = FontUtils.robotoFamily,
                fontWeight = FontWeight(400),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(textDesc) {
                    start.linkTo(box1.end, margin = 16.dp)
                    top.linkTo(textDate.bottom, margin = 10.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    width = Dimension.fillToConstraints
                })

        }
    }
}


@Preview
@Composable
fun previewError() {
    ErrorUICompose(Constants.SOMETHING_WENT_WRONG, action = "Retry", callBack = {})
}

