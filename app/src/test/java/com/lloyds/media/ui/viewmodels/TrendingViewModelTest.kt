package com.lloyds.media.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.usecase.TrendingUseCase
import com.lloyds.media.infra.local.Constants
import com.lloyds.media.ui.components.home.models.Pagination
import com.lloyds.media.ui.components.home.models.TrendingAction
import com.lloyds.media.ui.components.home.models.UiState
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

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 */
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TrendingViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @InjectMocks
    private lateinit var viewModel: TrendingViewModel

    @Mock
    private lateinit var useCase: TrendingUseCase

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
        viewModel.onAction(TrendingAction.retry)
        assertEquals(UiState<MutableList<MediaModel>>(true), viewModel.state.value)
    }

    @Test
    fun testUiStateWithData_WorkResultSuccess() = runTest {
        val data = mutableListOf<MediaModel>()
        viewModel.onAction(TrendingAction.retry)
        Mockito.`when`(useCase.getTrendingMedia()).thenReturn(
            Work.result(data)
        )
        delay(2000)
        assertEquals(
            UiState(
                false, null, data,
                pagination = Pagination.LOADING
            ), viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_WorkStop() = runTest {
        val data = Work.stop(message = Message(message = Constants.SOMETHING_WENT_WRONG))
        viewModel.onAction(TrendingAction.retry)
        Mockito.`when`(useCase.getTrendingMedia()).thenReturn(
            data
        )
        delay(2000)
        assertEquals(
            UiState<MutableList<MediaModel>>(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_Backfire() = runTest {
        val exception = RuntimeException(Constants.SOMETHING_WENT_WRONG)
        viewModel.onAction(TrendingAction.retry)
        Mockito.`when`(useCase.getTrendingMedia()).thenReturn(
            Work.backfire(exception)
        )
        delay(2000)
        assertEquals(
            UiState<MutableList<MediaModel>>(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

    @Test
    fun testUiStateWithData_Else() = runTest {
        viewModel.onAction(TrendingAction.retry)
        Mockito.`when`(useCase.getTrendingMedia()).thenReturn(
            Work.Cancelled
        )
        delay(2000)
        assertEquals(
            UiState<MutableList<MediaModel>>(false, Constants.SOMETHING_WENT_WRONG),
            viewModel.state.value
        )
    }

}