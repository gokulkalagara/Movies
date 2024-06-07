package com.lloyds.media.domain.usecase.impl

import com.lloyds.media.domain.models.MediaDetailsModel
import com.lloyds.media.domain.models.MovieDetailsModel
import com.lloyds.media.domain.models.Work
import com.lloyds.media.domain.models.getOriginalName
import com.lloyds.media.domain.repository.FavouritesRepository
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Optional

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 */

@RunWith(JUnit4::class)
class FavouritesUseCaseTest {
    @Mock
    private lateinit var repository: FavouritesRepository

    @InjectMocks
    private lateinit var useCaseImpl: FavouritesUseCaseImpl
    private lateinit var model: MediaDetailsModel
    private lateinit var entity: MediaFavouritesEntity

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCaseImpl = FavouritesUseCaseImpl(repository)
        model = MediaUseCaseTest.DummyMovieDetailsGeneratorObject.getDummyMovieDetails()
        entity = MediaFavouritesEntity(
            id = model.id,
            name = model.getOriginalName(),
            mediaType = if (model is MovieDetailsModel) "movie" else "tv",
            overview = model.overview,
            imageUrl = model.posterPath,
        )
    }

    @Test
    fun addToFavourites_Success() {
        runTest {
            Mockito.`when`(repository.addFavourites(entity)).thenReturn(entity)
            val work = useCaseImpl.addFavourites(model, entity.createdAt)
            val result = if (work is Work.Result) work.data else null
            assertEquals(entity, result)
        }
    }

    @Test
    fun addToFavourites_Failed() {
        runTest {
            val exception = RuntimeException("Media is already exist, Id : ${entity.id}")
            Mockito.`when`(repository.addFavourites(entity)).thenThrow(exception)
            val work = useCaseImpl.addFavourites(model, entity.createdAt)
            val backFireException = if (work is Work.Backfire) work.exception else null
            assertEquals(exception, backFireException)
        }
    }

    @Test
    fun isExistFavourites_Success() {
        runTest {
            val exceptedResult = true
            val mediaId = 123
            Mockito.`when`(repository.isExistFavourites(mediaId)).thenReturn(Optional.of(entity))
            val work = useCaseImpl.isExistFavourites(mediaId)
            val result = if (work is Work.Result) work.data else null
            assertEquals(exceptedResult, result)
        }
    }

    @Test
    fun isExistFavourites_Failed() {
        runTest {
            val exceptedResult = false
            val mediaId = 123
            Mockito.`when`(repository.isExistFavourites(mediaId)).thenReturn(Optional.empty())
            val work = useCaseImpl.isExistFavourites(mediaId)
            val result = if (work is Work.Result) work.data else null
            assertEquals(exceptedResult, result)
        }
    }


    @Test
    fun deleteFavourites_Success() {
        runTest {
            val exceptedResult = true
            val mediaId = 123
            Mockito.`when`(repository.deleteFavourites(mediaId)).thenReturn(true)
            val work = useCaseImpl.deleteFavourites(mediaId)
            val result = if (work is Work.Result) work.data else null
            assertEquals(exceptedResult, result)
        }
    }

    @Test
    fun deleteFavourites_Failed() {
        runTest {
            val exceptedResult = false
            val mediaId = 123
            Mockito.`when`(repository.deleteFavourites(mediaId)).thenReturn(false)
            val work = useCaseImpl.deleteFavourites(mediaId)
            val result = if (work is Work.Result) work.data else null
            assertEquals(exceptedResult, result)
        }
    }


    @Test
    fun getAllFavourites_SuccessItems() {
        runTest {
            val exceptedResult: MutableList<MediaFavouritesEntity> = mutableListOf()
            exceptedResult.add(entity)
            Mockito.`when`(repository.getAllFavourites()).thenReturn(exceptedResult)
            val work = useCaseImpl.getAllFavourites()
            val result = if (work is Work.Result) work.data else null
            assertEquals(exceptedResult, result)
        }
    }

    @Test
    fun getAllFavourites_Error_For_EmptyItems() {
        runTest {
            val exceptedResult = RuntimeException("There are currently no favourites available.")
            Mockito.`when`(repository.getAllFavourites()).thenReturn(emptyList())
            val work = useCaseImpl.getAllFavourites()
            val result = if (work is Work.Backfire) work.exception else null
            assertEquals(exceptedResult.message, result?.message)
        }
    }


}