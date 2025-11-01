package com.uniandes.vynilapp.data.remote

import com.uniandes.vynilapp.model.dto.ArtistAlbumDto
import com.uniandes.vynilapp.model.dto.ArtistDto
import com.uniandes.vynilapp.model.dto.PerformerPrizesDto
import com.uniandes.vynilapp.model.network.ApiService
import com.uniandes.vynilapp.model.services.ArtistServiceAdapter
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Response

class ArtistServiceAdapterTest {

    private lateinit var apiService: ApiService
    private lateinit var artistServiceAdapter: ArtistServiceAdapter

    @Before
    fun setup() {
        apiService = mockk()
        artistServiceAdapter = ArtistServiceAdapter(apiService)
    }

    @Test
    fun `getArtistById should return success when API call succeeds`() = runBlocking {
        // Arrange
        val artistId = 100
        val artistDto = createSampleArtistDto(artistId)
        val response = Response.success(artistDto)

        coEvery { apiService.getArtistById(artistId) } returns response

        // Act
        val result = artistServiceAdapter.getArtistById(artistId)

        // Assert
        assertTrue(result.isSuccess)
        val artist = result.getOrNull()
        assertNotNull(artist)
        assertEquals(artistId, artist?.id)
        assertEquals(artistDto.name, artist?.name)
        coVerify(exactly = 1) { apiService.getArtistById(artistId) }
    }

    @Test
    fun `getArtistById should return failure when API call fails`() = runBlocking {
        // Arrange
        val artistId = 100
        val response = Response.error<ArtistDto>(404, "Not Found".toResponseBody())

        coEvery { apiService.getArtistById(artistId) } returns response

        // Act
        val result = artistServiceAdapter.getArtistById(artistId)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("404") == true)
        coVerify(exactly = 1) { apiService.getArtistById(artistId) }
    }

    @Test
    fun `getArtistById should return failure when response body is null`() = runBlocking {
        // Arrange
        val artistId = 100
        val response = Response.success<ArtistDto>(null)

        coEvery { apiService.getArtistById(artistId) } returns response

        // Act
        val result = artistServiceAdapter.getArtistById(artistId)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("respuesta vacía") == true)
        coVerify(exactly = 1) { apiService.getArtistById(artistId) }
    }

    @Test
    fun `getArtistById should return failure when exception occurs`() = runBlocking {
        // Arrange
        val artistId = 100

        coEvery { apiService.getArtistById(artistId) } throws Exception("Network error")

        // Act
        val result = artistServiceAdapter.getArtistById(artistId)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Error de conexión") == true)
        coVerify(exactly = 1) { apiService.getArtistById(artistId) }
    }

    @Test
    fun `getArtistById should correctly convert DTO to domain model`() = runBlocking {
        // Arrange
        val artistId = 100
        val albums = listOf(
            createSampleAlbumDto(1, "Album 1"),
            createSampleAlbumDto(2, "Album 2")
        )
        val prizes = listOf(
            PerformerPrizesDto(1, "2020-01-01T00:00:00.000Z")
        )
        val artistDto = createSampleArtistDto(artistId, albums = albums, prizes = prizes)
        val response = Response.success(artistDto)

        coEvery { apiService.getArtistById(artistId) } returns response

        // Act
        val result = artistServiceAdapter.getArtistById(artistId)

        // Assert
        assertTrue(result.isSuccess)
        val artist = result.getOrNull()
        assertNotNull(artist)
        assertEquals(2, artist?.albums?.size)
        assertEquals(1, artist?.performerPrizes?.size)
        assertEquals("Album 1", artist?.albums?.get(0)?.name)
    }

    // Helper methods
    private fun createSampleArtistDto(
        id: Int,
        name: String = "Rubén Blades",
        albums: List<ArtistAlbumDto> = emptyList(),
        prizes: List<PerformerPrizesDto> = emptyList()
    ): ArtistDto {
        return ArtistDto(
            id = id,
            name = name,
            image = "https://example.com/artist.jpg",
            description = "Legendary artist",
            birthDate = "1948-07-16T00:00:00.000Z",
            albums = albums,
            performerPrizes = prizes
        )
    }

    private fun createSampleAlbumDto(id: Int, name: String): ArtistAlbumDto {
        return ArtistAlbumDto(
            id = id,
            name = name,
            cover = "https://example.com/album.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Great album",
            genre = "Salsa",
            recordLabel = "Elektra"
        )
    }
}