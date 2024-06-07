package com.lloyds.media.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.usecase.FavouritesUseCase
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.home.models.FavouritesAction
import com.lloyds.media.ui.components.home.models.FavouritesScreenUiState
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

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FavouritesViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: FavouritesViewModel
    @Mock
    private lateinit var useCase: FavouritesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUiStateLoading() = runTest {
        viewModel.onAction(FavouritesAction.retry)
        assertEquals(FavouritesScreenUiState(true), viewModel.state.value)
    }

    @Test
    fun testUiStateWithData_WorkResultSuccess() = runTest {
        val data = emptyList<MediaFavouritesEntity>()
        viewModel.onAction(FavouritesAction.retry)
        Mockito.`when`(useCase.getAllFavourites()).thenReturn(
            Work.result(data)
        )
        delay(2000)
        assertEquals(FavouritesScreenUiState(false, null, data), viewModel.state.value)
    }

    @Test
    fun testUiStateWithData_Backfire() = runTest {
        val exception = RuntimeException("There are currently no favourites available.")
        viewModel.onAction(FavouritesAction.retry)
        Mockito.`when`(useCase.getAllFavourites()).thenReturn(
            Work.backfire(exception)
        )
        delay(2000)
        assertEquals(
            FavouritesScreenUiState(false, exception.message),
            viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_Else() = runTest {
        viewModel.onAction(FavouritesAction.retry)
        Mockito.`when`(useCase.getAllFavourites()).thenReturn(
            Work.Cancelled
        )
        delay(2000)
        assertEquals(
            FavouritesScreenUiState(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

}