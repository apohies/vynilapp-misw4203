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

    @Test
    fun `getAllArtists should return success when API call is successful`() = runBlocking {

        val artistId = 1
        val artistsDtos = getArtistsMocks()
        val response = mockk<Response<List<ArtistDto>>>()

        every { response.isSuccessful } returns true
        every { response.body() } returns artistsDtos
        coEvery { apiService.getAllArtists() } returns response


        val result = artistServiceAdapter.getAllArtists()


        assertTrue(result.isSuccess)
        val artists = result.getOrNull()
        assertNotNull(artists)
        val artist0 = artists?.get(0)
        assertEquals(artistsDtos[0].id, artist0?.id)
        assertEquals(artistsDtos[0].name, artist0?.name)
        assertEquals(artistsDtos[0].image, artist0?.image)
        assertEquals(artistsDtos[0].birthDate, artist0?.birthDate)
        assertEquals(artistsDtos[0].description, artist0?.description)
        assertEquals(artistsDtos[0].albums, artist0?.albums)
        assertEquals(artistsDtos[0].performerPrizes, artist0?.performerPrizes)

        val artist1 = artists?.get(1)
        assertEquals(artistsDtos[1].id, artist1?.id)
        assertEquals(artistsDtos[1].name, artist1?.name)
        assertEquals(artistsDtos[1].image, artist1?.image)
        assertEquals(artistsDtos[1].birthDate, artist1?.birthDate)
        assertEquals(artistsDtos[1].description, artist1?.description)
        assertEquals(artistsDtos[1].albums, artist1?.albums)
        assertEquals(artistsDtos[1].performerPrizes, artist1?.performerPrizes)

        // Verify API call
        coVerify { apiService.getAllArtists() }
    }

    @Test
    fun `getAllArtists should return failure when API call fails`() = runBlocking {
        // Arrange
        val artistId = 100
        val response = Response.error<List<ArtistDto>>(404, "Not Found".toResponseBody())

        coEvery { apiService.getAllArtists() } returns response

        // Act
        val result = artistServiceAdapter.getAllArtists()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("404") == true)
        coVerify(exactly = 1) { apiService.getAllArtists() }
    }

    @Test
    fun `getAllArtists should return failure when exception occurs`() = runBlocking {
        // Arrange
        val artistId = 100

        coEvery { apiService.getAllArtists() } throws Exception("Network error")

        // Act
        val result = artistServiceAdapter.getAllArtists()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Network error") == true)
        coVerify(exactly = 1) { apiService.getAllArtists() }
    }

    private fun getArtistsMocks(): List<ArtistDto> {
        val artist1 = ArtistDto(
            id = 1,
            name = "Alvaro Diaz",
            image = "https://example.com/cover.jpg",
            birthDate = "1984-08-01T00:00:00.000Z",
            description = "Artista puertorriqueño de música urbana",
            albums = emptyList(),
            performerPrizes = emptyList()
        )

        val artist2 = ArtistDto(
            id = 2,
            name = "Frankie Ruíz",
            image = "https://example.com/cover.jpg",
            birthDate = "1958-03-10T00:00:00.000Z",
            description = "Fue un músico, cantante, compositor y director musical estadounidense de origen puertorriqueño.",
            albums = emptyList(),
            performerPrizes = emptyList()
        )

        val listOfArtists = listOf<ArtistDto>(artist1, artist2)
        return listOfArtists
    }
}