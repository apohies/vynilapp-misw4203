package com.uniandes.vynilapp.data.remote

import com.uniandes.vynilapp.model.dto.AlbumDto
import com.uniandes.vynilapp.model.dto.CommentDto
import com.uniandes.vynilapp.model.dto.PerformerDto
import com.uniandes.vynilapp.model.dto.TrackDto
import com.uniandes.vynilapp.model.network.ApiService
import com.uniandes.vynilapp.model.services.AlbumServiceAdapter
import com.uniandes.vynilapp.utils.CacheManager
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Response

class AlbumServiceAdapterTest {

    private lateinit var apiService: ApiService
    private lateinit var cacheManager: CacheManager
    private lateinit var albumServiceAdapter: AlbumServiceAdapter

    @Before
    fun setup() {
        apiService = mockk()
        cacheManager = mockk()
        albumServiceAdapter = AlbumServiceAdapter(apiService, cacheManager)
    }

    @Test
    fun `getAlbumById should return success when API call is successful`() = runBlocking {
        
        val albumId = 100
        val albumDto = createSampleAlbumDto()
        val response = mockk<Response<AlbumDto>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns albumDto
        coEvery { apiService.getAlbumById(albumId) } returns response

        
        val result = albumServiceAdapter.getAlbumById(albumId)

        
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
        
        val albumId = 100
        val response = mockk<Response<AlbumDto>>()
        
        every { response.isSuccessful } returns false
        every { response.code() } returns 404
        every { response.message() } returns "Not Found"
        coEvery { apiService.getAlbumById(albumId) } returns response

        
        val result = albumServiceAdapter.getAlbumById(albumId)

        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("Error al obtener álbum") == true)
        assertTrue(exception?.message?.contains("404") == true)
        
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should return failure when API call throws exception`() = runBlocking {
        
        val albumId = 100
        val exception = Exception("Network error")
        
        coEvery { apiService.getAlbumById(albumId) } throws exception

        
        val result = albumServiceAdapter.getAlbumById(albumId)

        
        assertTrue(result.isFailure)
        val resultException = result.exceptionOrNull()
        assertNotNull(resultException)
        assertTrue(resultException?.message?.contains("Error de conexión") == true)
        assertTrue(resultException?.message?.contains("Network error") == true)
        
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should return failure when response body is null`() = runBlocking {
        
        val albumId = 100
        val response = mockk<Response<AlbumDto>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        coEvery { apiService.getAlbumById(albumId) } returns response

        
        val result = albumServiceAdapter.getAlbumById(albumId)

        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("respuesta vacía") == true)
        
        coVerify { apiService.getAlbumById(albumId) }
    }

    @Test
    fun `getAllAlbums should return success when API call is successful`() = runBlocking {
        
        val albumDtos = listOf(createSampleAlbumDto(), createSampleAlbumDto2())
        val response = mockk<Response<List<AlbumDto>>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns albumDtos
        every { cacheManager.getAlbums() } returns null
        every { cacheManager.addAlbums(any()) } just Runs
        coEvery { apiService.getAllAlbums() } returns response

        
        val result = albumServiceAdapter.getAllAlbums()

        
        assertTrue(result.isSuccess)
        val albums = result.getOrNull()
        assertNotNull(albums)
        assertEquals(2, albums?.size)
        assertEquals(albumDtos[0].id, albums?.get(0)?.id)
        assertEquals(albumDtos[1].id, albums?.get(1)?.id)
        
        coVerify { apiService.getAllAlbums() }
        verify { cacheManager.addAlbums(any()) }
    }

    @Test
    fun `getAllAlbums should return cached albums when cache is available`() = runBlocking {
        
        val cachedAlbums = listOf(
            com.uniandes.vynilapp.model.Album(
                id = 100,
                name = "Cached Album",
                cover = "https://example.com/cover.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Cached description",
                genre = "Salsa",
                recordLabel = "Elektra",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        )
        
        every { cacheManager.getAlbums() } returns cachedAlbums

        
        val result = albumServiceAdapter.getAllAlbums()

        
        assertTrue(result.isSuccess)
        val albums = result.getOrNull()
        assertNotNull(albums)
        assertEquals(1, albums?.size)
        assertEquals(cachedAlbums[0].id, albums?.get(0)?.id)
        assertEquals(cachedAlbums[0].name, albums?.get(0)?.name)
        
        // Verify that API was not called
        coVerify(exactly = 0) { apiService.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should return failure when API call fails`() = runBlocking {
        
        val response = mockk<Response<List<AlbumDto>>>()
        
        every { response.isSuccessful } returns false
        every { response.code() } returns 500
        every { response.message() } returns "Internal Server Error"
        every { cacheManager.getAlbums() } returns null
        coEvery { apiService.getAllAlbums() } returns response

        
        val result = albumServiceAdapter.getAllAlbums()

        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("Error al obtener álbumes") == true)
        assertTrue(exception?.message?.contains("500") == true)
        
        coVerify { apiService.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should return failure when API call throws exception`() = runBlocking {
        
        val exception = Exception("Connection timeout")
        
        every { cacheManager.getAlbums() } returns null
        coEvery { apiService.getAllAlbums() } throws exception

        
        val result = albumServiceAdapter.getAllAlbums()

        
        assertTrue(result.isFailure)
        val resultException = result.exceptionOrNull()
        assertNotNull(resultException)
        assertTrue(resultException?.message?.contains("Error de conexión") == true)
        assertTrue(resultException?.message?.contains("Connection timeout") == true)
        
        coVerify { apiService.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should return failure when response body is null`() = runBlocking {
        
        val response = mockk<Response<List<AlbumDto>>>()
        
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        every { cacheManager.getAlbums() } returns null
        coEvery { apiService.getAllAlbums() } returns response

        
        val result = albumServiceAdapter.getAllAlbums()

        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("respuesta vacía") == true)
        
        coVerify { apiService.getAllAlbums() }
    }

    @Test
    fun `createAlbum should return success when API call is successful`() = runBlocking {
        // Arrange
        val albumDto = createSampleAlbumDto()
        val response = mockk<Response<AlbumDto>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns albumDto
        coEvery { apiService.createAlbum(any()) } returns response

        // Build an Album object matching fields (only fields used by convertToAlbumDto)
        val albumToCreate = com.uniandes.vynilapp.model.Album(
            id = 0,
            name = albumDto.name,
            cover = albumDto.cover,
            releaseDate = albumDto.releaseDate,
            description = albumDto.description,
            genre = albumDto.genre,
            recordLabel = albumDto.recordLabel,
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList()
        )

        // Act
        val result = albumServiceAdapter.createAlbum(albumToCreate)

        // Assert
        assertTrue(result.isSuccess)
        val created = result.getOrNull()
        assertNotNull(created)
        assertEquals(albumDto.id, created?.id)
        assertEquals(albumDto.name, created?.name)
        assertEquals(albumDto.cover, created?.cover)
        assertEquals(albumDto.releaseDate, created?.releaseDate)
        assertEquals(albumDto.description, created?.description)
        assertEquals(albumDto.genre, created?.genre)
        assertEquals(albumDto.recordLabel, created?.recordLabel)

        coVerify { apiService.createAlbum(any()) }
    }

    @Test
    fun `createAlbum should return failure when response body is null`() = runBlocking {
        // Arrange
        val response = mockk<Response<AlbumDto>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        coEvery { apiService.createAlbum(any()) } returns response

        val albumToCreate = com.uniandes.vynilapp.model.Album(
            id = 0, name = "X", cover = "", releaseDate = "", description = "", genre = "", recordLabel = "",
            tracks = emptyList(), performers = emptyList(), comments = emptyList()
        )

        // Act
        val result = albumServiceAdapter.createAlbum(albumToCreate)

        // Assert
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception?.message?.contains("respuesta vacía") == true)

        coVerify { apiService.createAlbum(any()) }
    }

    @Test
    fun `createAlbum should return failure when API throws exception`() = runBlocking {
        // Arrange
        val exception = Exception("Network down")
        coEvery { apiService.createAlbum(any()) } throws exception

        val albumToCreate = com.uniandes.vynilapp.model.Album(
            id = 0, name = "X", cover = "", releaseDate = "", description = "", genre = "", recordLabel = "",
            tracks = emptyList(), performers = emptyList(), comments = emptyList()
        )

        // Act
        val result = albumServiceAdapter.createAlbum(albumToCreate)

        // Assert
        assertTrue(result.isFailure)
        val resultEx = result.exceptionOrNull()
        assertNotNull(resultEx)
        assertTrue(resultEx?.message?.contains("Error de conexión") == true)
        assertTrue(resultEx?.message?.contains("Network down") == true)

        coVerify { apiService.createAlbum(any()) }
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
                    birthDate = "1948-07-16T00:00:00.000Z",
                    creationDate = null
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
