package com.lloyds.media.infra.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.lloyds.media.infra.database.MediaDatabase
import com.lloyds.media.infra.database.entity.MediaFavouritesEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * @Author: Gokul Kalagara
 * @Date: 07/06/24
 * copyright (c) 2024, All rights reserved.
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MediaFavouritesDaoTest {

    private lateinit var database: MediaDatabase
    private lateinit var dao: MediaFavouritesDao
    private lateinit var entity: MediaFavouritesEntity

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MediaDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getMediaFavouritesDao()
        entity = MediaFavouritesEntity(
            id = 1,
            name = "Salaar: Part 1 – Ceasefire",
            mediaType = "movie",
            overview = "The fate of a violently contested kingdom hangs on the fraught bond between two friends-turned-foes in this saga of power, bloodshed and betrayal.",
            imageUrl = "/test.png",
            createdAt = Date()
        )
    }

    @After
    fun end() {
        database.close()
    }

    @Test
    fun testInsertion_MediaFavouritesDao() = runTest {
        dao.insert(entity = entity)
        val items = dao.findAll()
        assertEquals(true, items.contains(entity))
    }

    @Test
    fun findByIdIsExist_MediaFavouritesDao() = runTest {
        dao.insert(entity = entity)
        val optional = dao.findById(entity.id)
        assertEquals(true, optional.isPresent)
    }

    @Test
    fun findByIdIsNotExist_MediaFavouritesDao() = runTest {
        val optional = dao.findById(789)
        assertEquals(false, optional.isPresent)
    }

    @Test
    fun deleteById_MediaFavouritesDao() = runTest {
        dao.insert(entity)
        dao.deleteById(entity.id)
        assertEquals(dao.findAll().size, 0)
    }

    @Test
    fun findAll_MediaFavouritesDao() = runTest {
        dao.insert(entity)
        assertEquals(dao.findAll().size, 1)
    }

}