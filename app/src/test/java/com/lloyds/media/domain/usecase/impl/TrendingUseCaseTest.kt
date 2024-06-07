package com.lloyds.media.domain.usecase.impl

import com.lloyds.media.data.models.TrendingMediaResponse
import com.lloyds.media.domain.models.MediaModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.repository.TrendingRepository
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
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

@RunWith(JUnit4::class)
class TrendingUseCaseTest {
    @Mock
    private lateinit var repository: TrendingRepository

    @InjectMocks
    private lateinit var useCaseImpl: TrendingUseCaseImpl
    private lateinit var response: TrendingMediaResponse

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCaseImpl = TrendingUseCaseImpl(repository)
        val dummyMedia = MediaModel(
            backdropPath = "/path/to/backdrop.jpg",
            id = 1,
            name = "Dummy Media",
            originalName = "Dummy Original Name",
            title = "Dummy Title",
            originalTitle = "Dummy Original Title",
            overview = "This is a dummy overview for the media.",
            posterPath = "/path/to/poster.jpg",
            mediaType = "movie",
            adult = false,
            originalLanguage = "en",
            genreIds = listOf(28, 12), // Action, Adventure genres
            popularity = 123.45,
            firstAirDate = "2024-01-01",
            releaseDate = "2024-01-01",
            voteAverage = 7.8,
            voteCount = 1000,
            originCountry = listOf("US")
        )

        response = TrendingMediaResponse(
            page = 1, totalPages = 10, results = listOf(
                dummyMedia, dummyMedia.copy(id = 2, name = "Dummy Media 2")
            ), // Multiple dummy items
            totalResults = 2
        )
    }

    @Test
    fun getTrendingMedia_ResultSuccess() {
        runBlocking {
            val expectedList = response.results
            Mockito.`when`(repository.getTrendingMedia(1)).thenReturn(
                NetworkResult.Success(response)
            )
            val work = useCaseImpl.getTrendingMedia()
            val result = if (work is Work.Result) work.data else emptyList()
            Assert.assertEquals(expectedList, result)
        }
    }

    @Test
    fun getTrendingMedia_ResultFailed() {
        runBlocking {
            val stopMessage = "unable to fetch trending media, page : 1"
            Mockito.`when`(repository.getTrendingMedia(1)).thenReturn(
                NetworkResult.Failed(FailedResponse(code = 401, errorResponse = null))
            )
            val work = useCaseImpl.getTrendingMedia()
            val result = if (work is Work.Stop) work.message.message else ""
            Assert.assertEquals(stopMessage, result)
        }
    }

    @Test
    fun getTrendingMedia_ResultBackFire() {
        runBlocking {
            val exceptionMessage = "Unable to resolve host"
            Mockito.`when`(repository.getTrendingMedia(1)).thenReturn(
                NetworkResult.Error(RuntimeException("Unable to resolve host"))
            )
            val work = useCaseImpl.getTrendingMedia()
            val result = if (work is Work.Backfire) work.exception.message else ""
            Assert.assertEquals(exceptionMessage, result)
        }
    }
}