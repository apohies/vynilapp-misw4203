package com.uniandes.vynilapp.data.repository

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.repository.ArtistRepository
import com.uniandes.vynilapp.model.services.ArtistServiceAdapter
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ArtistRepositoryTest {

    private lateinit var artistServiceAdapter: ArtistServiceAdapter
    private lateinit var artistRepository: ArtistRepository

    @Before
    fun setup() {
        artistServiceAdapter = mockk()
        artistRepository = ArtistRepository(artistServiceAdapter)
    }

    @Test
    fun `getAllArtists should delegate to service adapter`() = runBlocking {
        // Arrange
        val artists = listOf(createSampleArtist())
        val expectedResult = Result.success(artists)

        coEvery { artistServiceAdapter.getAllArtists() } returns expectedResult

        // Act
        val result = artistRepository.getAllArtists()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(artists, result.getOrNull())
        coVerify(exactly = 1) { artistServiceAdapter.getAllArtists() }
    }

    @Test
    fun `getAllArtists should propagate failure from service adapter`() = runBlocking {
        // Arrange
        val exception = Exception("Network error")
        val expectedResult = Result.failure<List<Artist>>(exception)

        coEvery { artistServiceAdapter.getAllArtists() } returns expectedResult

        // Act
        val result = artistRepository.getAllArtists()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { artistServiceAdapter.getAllArtists() }
    }

    @Test
    fun `getArtistById should delegate to service adapter with correct ID`() = runBlocking {
        // Arrange
        val artistId = 123
        val artist = createSampleArtist(id = artistId)
        val expectedResult = Result.success(artist)

        coEvery { artistServiceAdapter.getArtistById(artistId) } returns expectedResult

        // Act
        val result = artistRepository.getArtistById(artistId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(artist, result.getOrNull())
        assertEquals(artistId, result.getOrNull()?.id)
        coVerify(exactly = 1) { artistServiceAdapter.getArtistById(artistId) }
    }

    @Test
    fun `getArtistById should propagate failure from service adapter`() = runBlocking {
        // Arrange
        val artistId = 123
        val exception = Exception("Artist not found")
        val expectedResult = Result.failure<Artist>(exception)

        coEvery { artistServiceAdapter.getArtistById(artistId) } returns expectedResult

        // Act
        val result = artistRepository.getArtistById(artistId)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { artistServiceAdapter.getArtistById(artistId) }
    }

    @Test
    fun `multiple getArtistById calls should each delegate correctly`() = runBlocking {
        // Arrange
        val artistId1 = 100
        val artistId2 = 200
        val artist1 = createSampleArtist(id = artistId1)
        val artist2 = createSampleArtist(id = artistId2)

        coEvery { artistServiceAdapter.getArtistById(artistId1) } returns Result.success(artist1)
        coEvery { artistServiceAdapter.getArtistById(artistId2) } returns Result.success(artist2)

        // Act
        val result1 = artistRepository.getArtistById(artistId1)
        val result2 = artistRepository.getArtistById(artistId2)

        // Assert
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(artistId1, result1.getOrNull()?.id)
        assertEquals(artistId2, result2.getOrNull()?.id)
        coVerify(exactly = 1) { artistServiceAdapter.getArtistById(artistId1) }
        coVerify(exactly = 1) { artistServiceAdapter.getArtistById(artistId2) }
    }

    // Helper methods
    private fun createSampleArtist(id: Int = 100): Artist {
        return Artist(
            id = id,
            name = "Rubén Blades",
            image = "https://example.com/artist.jpg",
            description = "Legendary artist",
            birthDate = "1948-07-16T00:00:00.000Z",
            albums = emptyList(),
            performerPrizes = emptyList()
        )
    }
}