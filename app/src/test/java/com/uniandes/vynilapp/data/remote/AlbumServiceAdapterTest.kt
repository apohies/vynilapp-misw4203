package com.uniandes.vynilapp.data.remote

import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.model.Comment
import com.uniandes.vynilapp.data.model.Performer
import com.uniandes.vynilapp.data.model.Track
import com.uniandes.vynilapp.data.remote.dto.AlbumDto
import com.uniandes.vynilapp.data.remote.dto.CommentDto
import com.uniandes.vynilapp.data.remote.dto.PerformerDto
import com.uniandes.vynilapp.data.remote.dto.TrackDto
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Response

class AlbumServiceAdapterTest {

    private lateinit var apiService: ApiService
    private lateinit var albumServiceAdapter: AlbumServiceAdapter

    @Before
    fun setup() {
        apiService = mockk()
        albumServiceAdapter = AlbumServiceAdapter(apiService)
    }

    @Test
    fun `getAlbumById should return success when API call is successful`() = runBlocking {
        // Given
        val albumId = 100
        val albumDto = createSampleAlbumDto()
        val response = mockk<Response<AlbumDto>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns albumDto
        coEvery { apiService.getAlbumById(albumId) } returns response

        // When
        val result = albumServiceAdapter.getAlbumById(albumId)

        // Then
        assertTrue(result.isSuccess)
        val album = result.getOrNull()
        assertNotNull(album)
        assertEquals(albumDto.id, album?.id)
        assertEquals(albumDto.name, album?.name)
        assertEquals(albumDto.cover, album?.cover)
        assertEquals(albumDto.releaseDate, album?.releaseDate)
        assertEquals(albumDto.description, album?.description)
        assertEquals(albumDto.genre, album?.genre)
        assertEquals(albumDto.recordLabel, album?.recordLabel)
        
        // Verify API call
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should return failure when API call fails`() = runBlocking {
        // Given
        val albumId = 100
        val response = mockk<Response<AlbumDto>>()
        
        every { response.isSuccessful } returns false
        every { response.code() } returns 404
        every { response.message() } returns "Not Found"
        coEvery { apiService.getAlbumById(albumId) } returns response

        // When
        val result = albumServiceAdapter.getAlbumById(albumId)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("Error al obtener álbum") == true)
        assertTrue(exception?.message?.contains("404") == true)
        
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should return failure when API call throws exception`() = runBlocking {
        // Given
        val albumId = 100
        val exception = Exception("Network error")
        
        coEvery { apiService.getAlbumById(albumId) } throws exception

        // When
        val result = albumServiceAdapter.getAlbumById(albumId)

        // Then
        assertTrue(result.isFailure)
        val resultException = result.exceptionOrNull()
        assertNotNull(resultException)
        assertTrue(resultException?.message?.contains("Error de conexión") == true)
        assertTrue(resultException?.message?.contains("Network error") == true)
        
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should return failure when response body is null`() = runBlocking {
        // Given
        val albumId = 100
        val response = mockk<Response<AlbumDto>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        coEvery { apiService.getAlbumById(albumId) } returns response

        // When
        val result = albumServiceAdapter.getAlbumById(albumId)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("respuesta vacía") == true)
        
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAllAlbums should return success when API call is successful`() = runBlocking {
        // Given
        val albumDtos = listOf(createSampleAlbumDto(), createSampleAlbumDto2())
        val response = mockk<Response<List<AlbumDto>>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns albumDtos
        coEvery { apiService.getAllAlbums() } returns response

        // When
        val result = albumServiceAdapter.getAllAlbums()

        // Then
        assertTrue(result.isSuccess)
        val albums = result.getOrNull()
        assertNotNull(albums)
        assertEquals(2, albums?.size)
        assertEquals(albumDtos[0].id, albums?.get(0)?.id)
        assertEquals(albumDtos[1].id, albums?.get(1)?.id)
        
        coVerify { apiService.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should return failure when API call fails`() = runBlocking {
        // Given
        val response = mockk<Response<List<AlbumDto>>>()
        
        every { response.isSuccessful } returns false
        every { response.code() } returns 500
        every { response.message() } returns "Internal Server Error"
        coEvery { apiService.getAllAlbums() } returns response

        // When
        val result = albumServiceAdapter.getAllAlbums()

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("Error al obtener álbumes") == true)
        assertTrue(exception?.message?.contains("500") == true)
        
        coVerify { apiService.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should return failure when API call throws exception`() = runBlocking {
        // Given
        val exception = Exception("Connection timeout")
        
        coEvery { apiService.getAllAlbums() } throws exception

        // When
        val result = albumServiceAdapter.getAllAlbums()

        // Then
        assertTrue(result.isFailure)
        val resultException = result.exceptionOrNull()
        assertNotNull(resultException)
        assertTrue(resultException?.message?.contains("Error de conexión") == true)
        assertTrue(resultException?.message?.contains("Connection timeout") == true)
        
        coVerify { apiService.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should return failure when response body is null`() = runBlocking {
        // Given
        val response = mockk<Response<List<AlbumDto>>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        coEvery { apiService.getAllAlbums() } returns response

        // When
        val result = albumServiceAdapter.getAllAlbums()

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("respuesta vacía") == true)
        
        coVerify { apiService.getAllAlbums() }
    }

    // Helper methods to create test data
    private fun createSampleAlbumDto(): AlbumDto {
        return AlbumDto(
            id = 100,
            name = "Buscando América",
            cover = "https://example.com/cover.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Un álbum clásico de salsa",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = listOf(
                TrackDto(100, "Decisiones", "5:05"),
                TrackDto(101, "Desapariciones", "6:29")
            ),
            performers = listOf(
                PerformerDto(
                    id = 100,
                    name = "Rubén Blades",
                    image = "https://example.com/performer.jpg",
                    description = "Cantante panameño",
                    birthDate = "1948-07-16T00:00:00.000Z"
                )
            ),
            comments = listOf(
                CommentDto(100, "Excelente álbum", 5)
            )
        )
    }

    private fun createSampleAlbumDto2(): AlbumDto {
        return AlbumDto(
            id = 101,
            name = "Otro Álbum",
            cover = "https://example.com/cover2.jpg",
            releaseDate = "1985-01-01T00:00:00.000Z",
            description = "Otro álbum de prueba",
            genre = "Rock",
            recordLabel = "Sony",
            tracks = null,
            performers = null,
            comments = null
        )
    }
}
