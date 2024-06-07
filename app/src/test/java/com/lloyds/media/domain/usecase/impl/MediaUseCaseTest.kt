package com.lloyds.media.domain.usecase.impl

import com.lloyds.media.domain.models.CollectionDetails
import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.MovieDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.repository.MediaRepository
import com.lloyds.media.infra.network.model.FailedResponse
import com.lloyds.media.infra.network.model.NetworkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
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
class MediaUseCaseTest {
    @Mock
    private lateinit var repository: MediaRepository
    @InjectMocks
    private lateinit var useCaseImpl: MediaUseCaseImpl
    private lateinit var model: MediaDetailsModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        model = DummyMovieDetailsGeneratorObject.getDummyMovieDetails()
    }


    @Test
    fun getMediaDetails_ResultSuccess() {
        runBlocking {
            val mediaId = 1
            val mediaType = "movie"
            Mockito.`when`(repository.getMediaDetails(mediaId, mediaType)).thenReturn(
                NetworkResult.Success(model)
            )
            val work = useCaseImpl.getMediaDetails(mediaId, mediaType)
            val result = if (work is Work.Result) work.data else null
            assertEquals(model, result)
        }
    }


    @Test
    fun getMediaDetails_ResultFailed() {
        runBlocking {
            val mediaId = 1
            val mediaType = "movie"
            val stopMessage = "unable to fetch the media details for this id : $mediaId"
            Mockito.`when`(repository.getMediaDetails(mediaId, mediaType)).thenReturn(
                NetworkResult.Failed(FailedResponse(code = 401, errorResponse = null))
            )
            val work = useCaseImpl.getMediaDetails(1, "movie")
            val result = if (work is Work.Stop) work.message.message else ""
            assertEquals(stopMessage, result)
        }
    }

    @Test
    fun getMediaDetails_ResultBackFire() {
        runBlocking {
            val exceptionMessage = "Unable to resolve host"
            Mockito.`when`(repository.getMediaDetails(1, "movie")).thenReturn(
                NetworkResult.Error(RuntimeException("Unable to resolve host"))
            )
            val work = useCaseImpl.getMediaDetails(1, "movie")
            val result = if (work is Work.Backfire) work.exception.message else ""
            assertEquals(exceptionMessage, result)
        }
    }


    object DummyMovieDetailsGeneratorObject {
        fun getDummyMovieDetails(): MovieDetailsModel {
            val dummyCollectionDetails = CollectionDetails(
                id = 123,
                name = "Dummy Collection Name",
                posterPath = "/dummy/collection/poster/path.jpg",
                backdropPath = "/dummy/collection/backdrop/path.jpg"
            )

            return MovieDetailsModel(
                title = "Dummy Movie Title",
                originalTitle = "Dummy Original Movie Title",
                belongsToCollection = dummyCollectionDetails,
                budget = 150_000_000,
                releaseDate = "2024-12-31",
                revenue = 750_000_000L,
                runtime = 150,
                video = true
            )
        }
    }
}