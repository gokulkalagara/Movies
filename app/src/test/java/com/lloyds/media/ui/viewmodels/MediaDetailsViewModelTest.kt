package com.lloyds.media.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.MovieDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.models.getOriginalName
import com.lloyds.media.domain.usecase.FavouritesUseCase
import com.lloyds.media.domain.usecase.MediaUseCase
import com.lloyds.media.domain.usecase.impl.MediaUseCaseTest
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.details.models.MediaDetailsAction
import com.lloyds.media.ui.components.details.models.MediaDetailsScreenUiState
import com.lloyds.media.ui.viewmodels.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Date

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MediaDetailsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val mediaId = -1
    private val mediaType = "movie"

    @InjectMocks
    private lateinit var viewModel: MediaDetailsViewModel

    @Mock
    private lateinit var useCase: MediaUseCase

    @Mock
    private lateinit var favouritesUseCase: FavouritesUseCase

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var model: MediaDetailsModel
    private lateinit var entity: MediaFavouritesEntity

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockitoAnnotations.openMocks(this)
        model = MediaUseCaseTest.DummyMovieDetailsGeneratorObject.getDummyMovieDetails()
        entity = MediaFavouritesEntity(
            id = model.id,
            name = model.getOriginalName(),
            mediaType = if (model is MovieDetailsModel) "movie" else "tv",
            overview = model.overview,
            imageUrl = model.posterPath,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUiStateLoading() = runTest {
        viewModel.onAction(MediaDetailsAction.retry)
        assertEquals(MediaDetailsScreenUiState(true), viewModel.state.value)
    }

    @Test
    fun testUiStateWithData_WorkResultSuccess() = runTest {
        Mockito.`when`(useCase.getMediaDetails(mediaId, mediaType)).thenReturn(
            Work.result(model)
        )
        viewModel.onAction(MediaDetailsAction.retry)
        delay(2000)
        assertEquals(
            MediaDetailsScreenUiState(false, null, model),
            viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_WorkStop() = runTest {
        val data = Work.stop(message = Message(message = Constants.SOMETHING_WENT_WRONG))
        viewModel.onAction(MediaDetailsAction.retry)
        Mockito.`when`(useCase.getMediaDetails(mediaId, mediaType)).thenReturn(
            data
        )
        delay(2000)
        assertEquals(
            MediaDetailsScreenUiState(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_Backfire() = runTest {
        val exception = RuntimeException(Constants.SOMETHING_WENT_WRONG)
        viewModel.onAction(MediaDetailsAction.retry)
        Mockito.`when`(useCase.getMediaDetails(mediaId, mediaType)).thenReturn(
            Work.backfire(exception)
        )
        delay(2000)
        assertEquals(
            MediaDetailsScreenUiState(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_Else() = runTest {
        viewModel.onAction(MediaDetailsAction.retry)
        Mockito.`when`(useCase.getMediaDetails(mediaId, mediaType)).thenReturn(
            Work.Cancelled
        )
        delay(2000)
        assertEquals(
            MediaDetailsScreenUiState(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

    @Test
    fun test_AddFavourites() = runTest {
        viewModel.onAction(MediaDetailsAction.FavAction(model, true))
        Mockito.`when`(favouritesUseCase.addFavourites(model, createdAt = Date())).thenReturn(
            Work.Result(entity)
        )
    }

    @Test
    fun test_DeleteFavourites() = runTest {
        viewModel.onAction(MediaDetailsAction.FavAction(model, true))
        Mockito.`when`(favouritesUseCase.deleteFavourites(model.id)).thenReturn(
            Work.Result(true)
        )
    }

}