package com.lloyds.media.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lloyds.media.R
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import com.lloyds.media.ui.components.ErrorUICompose
import com.lloyds.media.ui.components.ProgressBarCompose
import com.lloyds.media.ui.components.home.models.FavouritesAction
import com.lloyds.media.ui.components.home.models.FavouritesScreenUiState
import com.lloyds.media.ui.viewmodels.FavouritesViewModel
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
fun FavouritesScreen(viewModel: FavouritesViewModel = hiltViewModel()) {
    val uiState = remember {
        mutableStateOf(FavouritesScreenUiState())
    }
    LaunchedEffect(viewModel) {
        viewModel.state.collect {
            uiState.value = it
        }
    }
    FavouritesScreenContent(uiState.value) {
        when (it) {
            FavouritesAction.retry -> {
                viewModel.onAction(it)
            }
        }
    }
}

@Composable
fun FavouritesScreenContent(uiState: FavouritesScreenUiState, action: (FavouritesAction) -> Unit) {
    if (uiState.isLoading) {
        ProgressBarCompose()
    } else if ((uiState.error?.length ?: 0) > 0) {
        ErrorUICompose(uiState.error ?: "", action = "Refresh", callBack = {
            action(FavouritesAction.retry)
        })
    } else {
        FavouritesMediaList(uiState.list)
    }
}

@Composable
fun FavouritesMediaList(list: List<MediaFavouritesEntity>) {
    LazyColumn {
        items(list) {
            FavouritesItem(it)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FavouritesItem(entity: MediaFavouritesEntity) {
    Surface(
        shadowElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 1.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(3.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {

            val (box1, text1, text2, image1) = createRefs()
            AsyncImage(
                model = UrlUtils.getImageUrl(entity.imageUrl, ImageDim.W200),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .constrainAs(box1) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(
                        color = colorResource(id = R.color.light_tint_gray),
                        shape = CircleShape
                    ).padding(1.dp).clip(CircleShape)
            )

            Text(text = entity.name,
                fontSize = 16.sp,
                fontFamily = FontUtils.robotoFamily,
                fontWeight = FontWeight(600),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = TextUnit(0.5F, TextUnitType.Sp),
                modifier = Modifier.constrainAs(text1) {
                    start.linkTo(box1.end, margin = 16.dp)
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(image1.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                })

            Text(text = entity.mediaType?.uppercase() ?: "",
                color = colorResource(id = R.color.light_gray),
                fontSize = 10.sp,
                fontFamily = FontUtils.robotoFamily,
                fontWeight = FontWeight(400),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = TextUnit(0.5F, TextUnitType.Sp),
                modifier = Modifier.constrainAs(text2) {
                    start.linkTo(box1.end, margin = 16.dp)
                    top.linkTo(text1.bottom, margin = 5.dp)
                    end.linkTo(image1.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                })

            Image(painter = painterResource(id = R.drawable.ic_forward),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = colorResource(id = R.color.light_gray)),
                modifier = Modifier.width(24.dp).constrainAs(image1) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                })
        }
    }
}

