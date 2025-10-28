package com.uniandes.vynilapp.data.repository

import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.model.Comment
import com.uniandes.vynilapp.data.model.Performer
import com.uniandes.vynilapp.data.model.Track
import com.uniandes.vynilapp.data.remote.AlbumServiceAdapter
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class AlbumRepositoryTest {

    private lateinit var albumServiceAdapter: AlbumServiceAdapter
    private lateinit var albumRepository: AlbumRepository

    @Before
    fun setup() {
        albumServiceAdapter = mockk()
        albumRepository = AlbumRepository(albumServiceAdapter)
    }

    @Test
    fun `getAllAlbums should delegate to service adapter and return success`() = runBlocking {
        
        val expectedAlbums = listOf(createSampleAlbum(), createSampleAlbum2())
        val successResult = Result.success(expectedAlbums)
        
        coEvery { albumServiceAdapter.getAllAlbums() } returns successResult

        
        val result = albumRepository.getAllAlbums()

        
        assertTrue(result.isSuccess)
        val albums = result.getOrNull()
        assertNotNull(albums)
        assertEquals(2, albums?.size)
        assertEquals(expectedAlbums[0].id, albums?.get(0)?.id)
        assertEquals(expectedAlbums[1].id, albums?.get(1)?.id)
        
        coVerify { albumServiceAdapter.getAllAlbums() }
    }

    @Test
    fun `getAllAlbums should delegate to service adapter and return failure`() = runBlocking {
        
        val exception = Exception("Service error")
        val failureResult = Result.failure<List<Album>>(exception)
        
        coEvery { albumServiceAdapter.getAllAlbums() } returns failureResult

        
        val result = albumRepository.getAllAlbums()

        
        assertTrue(result.isFailure)
        val resultException = result.exceptionOrNull()
        assertNotNull(resultException)
        assertEquals(exception.message, resultException?.message)
        
        coVerify { albumServiceAdapter.getAllAlbums() }
    }

    @Test
    fun `getAlbumById should delegate to service adapter and return success`() = runBlocking {
        
        val albumId = 100
        val expectedAlbum = createSampleAlbum()
        val successResult = Result.success(expectedAlbum)
        
        coEvery { albumServiceAdapter.getAlbumById(albumId) } returns successResult

        
        val result = albumRepository.getAlbumById(albumId)

        
        assertTrue(result.isSuccess)
        val album = result.getOrNull()
        assertNotNull(album)
        assertEquals(expectedAlbum.id, album?.id)
        assertEquals(expectedAlbum.name, album?.name)
        assertEquals(expectedAlbum.cover, album?.cover)
        assertEquals(expectedAlbum.releaseDate, album?.releaseDate)
        assertEquals(expectedAlbum.description, album?.description)
        assertEquals(expectedAlbum.genre, album?.genre)
        assertEquals(expectedAlbum.recordLabel, album?.recordLabel)
        
        coVerify { albumServiceAdapter.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should delegate to service adapter and return failure`() = runBlocking {
        
        val albumId = 100
        val exception = Exception("Album not found")
        val failureResult = Result.failure<Album>(exception)
        
        coEvery { albumServiceAdapter.getAlbumById(albumId) } returns failureResult

        
        val result = albumRepository.getAlbumById(albumId)

        
        assertTrue(result.isFailure)
        val resultException = result.exceptionOrNull()
        assertNotNull(resultException)
        assertEquals(exception.message, resultException?.message)
        
        coVerify { albumServiceAdapter.getAlbumById(albumId) }
    }

    @Test
    fun `getAlbumById should call service adapter with correct album ID`() = runBlocking {
        
        val albumId = 123
        val expectedAlbum = createSampleAlbum()
        val successResult = Result.success(expectedAlbum)
        
        coEvery { albumServiceAdapter.getAlbumById(albumId) } returns successResult

        
        albumRepository.getAlbumById(albumId)

        
        coVerify(exactly = 1) { albumServiceAdapter.getAlbumById(albumId) }
    }

    @Test
    fun `getAllAlbums should call service adapter exactly once`() = runBlocking {
        
        val expectedAlbums = listOf(createSampleAlbum())
        val successResult = Result.success(expectedAlbums)
        
        coEvery { albumServiceAdapter.getAllAlbums() } returns successResult

        
        albumRepository.getAllAlbums()

        
        coVerify(exactly = 1) { albumServiceAdapter.getAllAlbums() }
    }

    @Test
    fun `repository should handle multiple calls correctly`() = runBlocking {
        
        val albumId = 100
        val album = createSampleAlbum()
        val albums = listOf(album)
        
        val albumSuccessResult = Result.success(album)
        val albumsSuccessResult = Result.success(albums)
        
        coEvery { albumServiceAdapter.getAlbumById(albumId) } returns albumSuccessResult
        coEvery { albumServiceAdapter.getAllAlbums() } returns albumsSuccessResult

        
        val albumResult = albumRepository.getAlbumById(albumId)
        val albumsResult = albumRepository.getAllAlbums()

        
        assertTrue(albumResult.isSuccess)
        assertTrue(albumsResult.isSuccess)
        
        assertEquals(album.id, albumResult.getOrNull()?.id)
        assertEquals(1, albumsResult.getOrNull()?.size)
        
        coVerify { albumServiceAdapter.getAlbumById(albumId) }
        coVerify { albumServiceAdapter.getAllAlbums() }
    }

    // Helper methods to create test data
    private fun createSampleAlbum(): Album {
        return Album(
            id = 100,
            name = "Buscando América",
            cover = "https://example.com/cover.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Un álbum clásico de salsa",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = listOf(
                Track(
                    id = 100,
                    name = "Decisiones",
                    duration = "5:05"
                ),
                Track(
                    id = 101,
                    name = "Desapariciones",
                    duration = "6:29"
                )
            ),
            performers = listOf(
                Performer(
                    id = 100,
                    name = "Rubén Blades",
                    image = "https://example.com/performer.jpg",
                    description = "Cantante panameño",
                    birthDate = "1948-07-16T00:00:00.000Z"
                )
            ),
            comments = listOf(
                Comment(
                    id = 100,
                    description = "Excelente álbum",
                    rating = 5
                )
            )
        )
    }

    private fun createSampleAlbum2(): Album {
        return Album(
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
