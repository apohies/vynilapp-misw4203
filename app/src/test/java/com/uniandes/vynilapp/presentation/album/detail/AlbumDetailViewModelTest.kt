package com.uniandes.vynilapp.presentation.album.detail

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Comment
import com.uniandes.vynilapp.model.Performer
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.model.repository.AlbumRepository
import com.uniandes.vynilapp.viewModels.albums.AlbumDetailViewModel
import com.uniandes.vynilapp.views.states.AlbumDetailEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import com.uniandes.vynilapp.model.dto.AddCommentResponse
import com.uniandes.vynilapp.model.dto.AlbumDto
import com.uniandes.vynilapp.model.dto.CollectorDto

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumDetailViewModelTest {

    private lateinit var albumRepository: AlbumRepository
    private lateinit var viewModel: AlbumDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        albumRepository = mockk()
        viewModel = AlbumDetailViewModel(albumRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAlbumById should update UI state with album data when successful`() = runTest {

        val albumId = 100
        val album = createSampleAlbum()
        val successResult = Result.success(album)

        coEvery { albumRepository.getAlbumById(albumId) } returns successResult


        viewModel.loadAlbumById(albumId)


        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
        assertNotNull(uiState.album)
        assertEquals(album.id, uiState.album?.id)
        assertEquals(album.name, uiState.album?.name)
        assertEquals(album.tracks?.size, uiState.tracks.size)
        assertEquals(album.comments?.size, uiState.comments.size)

        coVerify { albumRepository.getAlbumById(albumId) }
    }

    @Test
    fun `loadAlbumById should update UI state with error when repository fails`() = runTest {

        val albumId = 100
        val exception = Exception("Network error")
        val failureResult = Result.failure<Album>(exception)

        coEvery { albumRepository.getAlbumById(albumId) } returns failureResult


        viewModel.loadAlbumById(albumId)


        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.error)
        assertTrue(uiState.error?.contains("Error al cargar el álbum") == true)
        assertNull(uiState.album)

        coVerify { albumRepository.getAlbumById(albumId) }
    }

    @Test
    fun `loadAlbumById should set loading state during API call`() = runTest {

        val albumId = 100
        val album = createSampleAlbum()
        val successResult = Result.success(album)

        coEvery { albumRepository.getAlbumById(albumId) } returns successResult


        viewModel.loadAlbumById(albumId)


        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading) // Should be false after completion
        assertNull(uiState.error)

        coVerify { albumRepository.getAlbumById(albumId) }
    }

    @Test
    fun `onEvent LoadAlbumById should call loadAlbumById with correct ID`() = runTest {

        val albumId = 123
        val album = createSampleAlbum()
        val successResult = Result.success(album)

        coEvery { albumRepository.getAlbumById(albumId) } returns successResult


        viewModel.onEvent(AlbumDetailEvent.LoadAlbumById(albumId))


        coVerify { albumRepository.getAlbumById(albumId) }
    }

    @Test
    fun `onEvent LoadAlbum should call loadAlbumById with current album ID`() = runTest {

        val albumId = 100
        val album = createSampleAlbum()
        val successResult = Result.success(album)

        coEvery { albumRepository.getAlbumById(albumId) } returns successResult


        viewModel.loadAlbumById(albumId) // Set current album ID
        viewModel.onEvent(AlbumDetailEvent.LoadAlbum)


        coVerify(exactly = 2) { albumRepository.getAlbumById(albumId) }
    }

    @Test
    fun `toggleLike should update isLiked state`() = runTest {

        val initialUiState = viewModel.uiState.value
        assertFalse(initialUiState.isLiked)


        viewModel.onEvent(AlbumDetailEvent.ToggleLike)


        val updatedUiState = viewModel.uiState.value
        assertTrue(updatedUiState.isLiked)
    }

    @Test
    fun `toggleSave should update isSaved state`() = runTest {

        val initialUiState = viewModel.uiState.value
        assertFalse(initialUiState.isSaved)


        viewModel.onEvent(AlbumDetailEvent.ToggleSave)


        val updatedUiState = viewModel.uiState.value
        assertTrue(updatedUiState.isSaved)
    }

    @Test
    fun `playAlbum should update isPlaying state`() = runTest {

        val initialUiState = viewModel.uiState.value
        assertFalse(initialUiState.isPlaying)


        viewModel.onEvent(AlbumDetailEvent.PlayAlbum)


        val updatedUiState = viewModel.uiState.value
        assertTrue(updatedUiState.isPlaying)
    }

    @Test
    fun `pauseAlbum should update isPlaying state to false`() = runTest {

        viewModel.onEvent(AlbumDetailEvent.PlayAlbum) // Set to playing first
        assertTrue(viewModel.uiState.value.isPlaying)


        viewModel.onEvent(AlbumDetailEvent.PauseAlbum)


        val updatedUiState = viewModel.uiState.value
        assertFalse(updatedUiState.isPlaying)
    }


    @Test
    fun `addComment should add comment to comments list`() = runTest {

        val commentText = "Excelente álbum"

        var album = AlbumDto(
            id = 100,
            name = "Buscando América",
            cover = "https://example.com/cover.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Un álbum clásico de salsa",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = listOf(),
            performers = listOf(),
            comments = listOf()


        )

        var colector = CollectorDto(
            id = 100,
            name = "Rubén Blades",
            telephone = "331321",
            email = "william.a.wheeler@example-pet-store.com",
            comments = listOf(),
            favoritePerformers = listOf(),
            collectorAlbums = listOf()

        )

        val mockResponse = AddCommentResponse(
            id = 999,
            description = commentText,
            rating = 5,
            collector = colector,
            album = album
        )

        coEvery {
            albumRepository.AddCommentToAlbum(
                albumId = any(),
                description = any(),
                rating = any(),
                collectorId = any()
            )
        } returns Result.success(mockResponse)


        val initialCommentsCount = viewModel.uiState.value.comments.size


        viewModel.onEvent(AlbumDetailEvent.AddComment(commentText, 5))


        advanceUntilIdle()


        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCommentsCount + 1, updatedUiState.comments.size)
        assertTrue(updatedUiState.comments.any { it.description == commentText })


        coVerify {
            albumRepository.AddCommentToAlbum(
                albumId = any(),
                description = commentText,
                rating = 5,
                collectorId = 100
            )
        }
    }

    @Test
    fun `updateCommentText should update newCommentText`() = runTest {

        val commentText = "Mi comentario"


        viewModel.onEvent(AlbumDetailEvent.UpdateCommentText(commentText))


        val updatedUiState = viewModel.uiState.value
        assertEquals(commentText, updatedUiState.newCommentText)
    }

    @Test
    fun `getCurrentAlbumId should return current album ID`() = runTest {

        val albumId = 123
        viewModel.loadAlbumById(albumId)


        val currentId = viewModel.getCurrentAlbumId()


        assertEquals(albumId, currentId)
    }

    @Test
    fun `refreshAlbum should reload current album`() = runTest {

        val albumId = 100
        val album = createSampleAlbum()
        val successResult = Result.success(album)

        coEvery { albumRepository.getAlbumById(albumId) } returns successResult


        viewModel.loadAlbumById(albumId) // Set current album ID
        viewModel.refreshAlbum()


        coVerify(exactly = 2) { albumRepository.getAlbumById(albumId) }
    }

    // Helper method to create test data
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
    @Test
    fun `addComment should handle repository failure`() = runTest {
        val exception = Exception("Network error")
        coEvery {
            albumRepository.AddCommentToAlbum(
                albumId = any(),
                description = any(),
                rating = any(),
                collectorId = any()
            )
        } returns Result.failure(exception)

        val initialCommentsCount = viewModel.uiState.value.comments.size

        viewModel.onEvent(AlbumDetailEvent.AddComment("Test comment", 5))
        advanceUntilIdle()

        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCommentsCount, updatedUiState.comments.size)
        assertNotNull(updatedUiState.error)
        assertTrue(updatedUiState.error?.contains("Error al agregar comentario") == true)
    }

    @Test
    fun `addComment should not add empty comment`() = runTest {
        val initialCommentsCount = viewModel.uiState.value.comments.size

        viewModel.onEvent(AlbumDetailEvent.AddComment("", 5))
        advanceUntilIdle()

        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCommentsCount, updatedUiState.comments.size)

        coVerify(exactly = 0) {
            albumRepository.AddCommentToAlbum(any(), any(), any(), any())
        }
    }

    @Test
    fun `addComment should not add comment with only whitespace`() = runTest {
        val initialCommentsCount = viewModel.uiState.value.comments.size

        viewModel.onEvent(AlbumDetailEvent.AddComment("   ", 5))
        advanceUntilIdle()

        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCommentsCount, updatedUiState.comments.size)

        coVerify(exactly = 0) {
            albumRepository.AddCommentToAlbum(any(), any(), any(), any())
        }
    }

    @Test
    fun `addComment should clear comment text after successful addition`() = runTest {
        val commentText = "Great album!"

        val mockResponse = AddCommentResponse(
            id = 999,
            description = commentText,
            rating = 4,
            collector = CollectorDto(
                id = 100,
                name = "Test Collector",
                telephone = "123456",
                email = "test@test.com",
                comments = listOf(),
                favoritePerformers = listOf(),
                collectorAlbums = listOf()
            ),
            album = AlbumDto(
                id = 100,
                name = "Test Album",
                cover = "https://example.com/cover.jpg",
                releaseDate = "2024-01-01T00:00:00.000Z",
                description = "Test description",
                genre = "Rock",
                recordLabel = "Test Label",
                tracks = listOf(),
                performers = listOf(),
                comments = listOf()
            )
        )

        coEvery {
            albumRepository.AddCommentToAlbum(any(), any(), any(), any())
        } returns Result.success(mockResponse)

        viewModel.onEvent(AlbumDetailEvent.UpdateCommentText(commentText))
        assertEquals(commentText, viewModel.uiState.value.newCommentText)

        viewModel.onEvent(AlbumDetailEvent.AddComment(commentText, 4))
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.newCommentText)
    }

    @Test
    fun `addComment should handle different rating values`() = runTest {
        val ratings = listOf(1, 2, 3, 4, 5)

        ratings.forEach { rating ->
            val mockResponse = AddCommentResponse(
                id = rating,
                description = "Comment with rating $rating",
                rating = rating,
                collector = CollectorDto(
                    id = 100,
                    name = "Test",
                    telephone = "123",
                    email = "test@test.com",
                    comments = listOf(),
                    favoritePerformers = listOf(),
                    collectorAlbums = listOf()
                ),
                album = AlbumDto(
                    id = 100,
                    name = "Test",
                    cover = "",
                    releaseDate = "",
                    description = "",
                    genre = "",
                    recordLabel = "",
                    tracks = listOf(),
                    performers = listOf(),
                    comments = listOf()
                )
            )

            coEvery {
                albumRepository.AddCommentToAlbum(any(), any(), eq(rating), any())
            } returns Result.success(mockResponse)

            viewModel.onEvent(AlbumDetailEvent.AddComment("Test comment", rating))
            advanceUntilIdle()

            coVerify {
                albumRepository.AddCommentToAlbum(any(), any(), rating, any())
            }
        }
    }

    @Test
    fun `addComment should set loading state during API call`() = runTest {
        val commentText = "Test comment"
        var wasLoadingSet = false

        val mockResponse = AddCommentResponse(
            id = 1,
            description = commentText,
            rating = 5,
            collector = CollectorDto(
                id = 100,
                name = "Test",
                telephone = "123",
                email = "test@test.com",
                comments = listOf(),
                favoritePerformers = listOf(),
                collectorAlbums = listOf()
            ),
            album = AlbumDto(
                id = 100,
                name = "Test",
                cover = "",
                releaseDate = "",
                description = "",
                genre = "",
                recordLabel = "",
                tracks = listOf(),
                performers = listOf(),
                comments = listOf()
            )
        )

        coEvery {
            albumRepository.AddCommentToAlbum(any(), any(), any(), any())
        } coAnswers {
            if (viewModel.uiState.value.isLoading) {
                wasLoadingSet = true
            }
            Result.success(mockResponse)
        }

        viewModel.onEvent(AlbumDetailEvent.AddComment(commentText, 5))
        advanceUntilIdle()

        assertTrue(wasLoadingSet)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `addComment should use correct collectorId`() = runTest {
        val commentText = "Test comment"
        val expectedCollectorId = 100

        val mockResponse = AddCommentResponse(
            id = 1,
            description = commentText,
            rating = 5,
            collector = CollectorDto(
                id = expectedCollectorId,
                name = "Test",
                telephone = "123",
                email = "test@test.com",
                comments = listOf(),
                favoritePerformers = listOf(),
                collectorAlbums = listOf()
            ),
            album = AlbumDto(
                id = 100,
                name = "Test",
                cover = "",
                releaseDate = "",
                description = "",
                genre = "",
                recordLabel = "",
                tracks = listOf(),
                performers = listOf(),
                comments = listOf()
            )
        )

        coEvery {
            albumRepository.AddCommentToAlbum(any(), any(), any(), any())
        } returns Result.success(mockResponse)

        viewModel.onEvent(AlbumDetailEvent.AddComment(commentText, 5))
        advanceUntilIdle()

        coVerify {
            albumRepository.AddCommentToAlbum(
                albumId = any(),
                description = commentText,
                rating = 5,
                collectorId = expectedCollectorId
            )
        }
    }

    @Test
    fun `addComment should trim whitespace from comment text`() = runTest {
        val commentWithSpaces = "  Test comment with spaces  "
        val trimmedComment = "Test comment with spaces"

        val mockResponse = AddCommentResponse(
            id = 1,
            description = trimmedComment,
            rating = 5,
            collector = CollectorDto(
                id = 100,
                name = "Test",
                telephone = "123",
                email = "test@test.com",
                comments = listOf(),
                favoritePerformers = listOf(),
                collectorAlbums = listOf()
            ),
            album = AlbumDto(
                id = 100,
                name = "Test",
                cover = "",
                releaseDate = "",
                description = "",
                genre = "",
                recordLabel = "",
                tracks = listOf(),
                performers = listOf(),
                comments = listOf()
            )
        )

        coEvery {
            albumRepository.AddCommentToAlbum(any(), any(), any(), any())
        } returns Result.success(mockResponse)

        viewModel.onEvent(AlbumDetailEvent.AddComment(commentWithSpaces, 5))
        advanceUntilIdle()

        coVerify {
            albumRepository.AddCommentToAlbum(
                albumId = any(),
                description = trimmedComment,
                rating = any(),
                collectorId = any()
            )
        }
    }

    // Agregar estos tests a AlbumDetailViewModelTest.kt

    @Test
    fun `addTrack should add track to tracks list`() = runTest {
        // Given
        val trackName = "Decisiones"
        val trackDuration = "5:05"
        val track = Track(
            id = 0,
            name = trackName,
            duration = trackDuration
        )

        val savedTrack = Track(
            id = 999,
            name = trackName,
            duration = trackDuration
        )

        coEvery {
            albumRepository.addTrackToAlbum(
                albumId = any(),
                track = any()
            )
        } returns Result.success(savedTrack)

        val initialTracksCount = viewModel.uiState.value.tracks.size


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))
        advanceUntilIdle()


        val updatedUiState = viewModel.uiState.value
        assertEquals(initialTracksCount + 1, updatedUiState.tracks.size)
        assertTrue(updatedUiState.tracks.any { it.name == trackName })

        coVerify {
            albumRepository.addTrackToAlbum(
                albumId = any(),
                track = track
            )
        }
    }

    @Test
    fun `addTrack should add track immediately with optimistic update`() = runTest {
        // Given
        val track = Track(
            id = 0,
            name = "Test Track",
            duration = "3:45"
        )

        val savedTrack = Track(
            id = 999,
            name = "Test Track",
            duration = "3:45"
        )


        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } coAnswers {
            kotlinx.coroutines.delay(100)
            Result.success(savedTrack)
        }

        val initialTracksCount = viewModel.uiState.value.tracks.size


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))


        val uiStateBeforeApiResponse = viewModel.uiState.value
        assertEquals(initialTracksCount + 1, uiStateBeforeApiResponse.tracks.size)

        advanceUntilIdle()


        val uiStateAfterApiResponse = viewModel.uiState.value
        assertEquals(initialTracksCount + 1, uiStateAfterApiResponse.tracks.size)
        assertTrue(uiStateAfterApiResponse.tracks.any { it.id == 999 })
    }

    @Test
    fun `addTrack should replace temporary track with server track on success`() = runTest {

        val track = Track(
            id = 0,
            name = "Test Track",
            duration = "3:45"
        )

        val savedTrack = Track(
            id = 999,
            name = "Test Track",
            duration = "3:45"
        )

        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } returns Result.success(savedTrack)


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))
        advanceUntilIdle()

        val updatedUiState = viewModel.uiState.value
        assertTrue(updatedUiState.tracks.any { it.id == 999 })
        assertFalse(updatedUiState.tracks.any { it.id == 0 })
    }

    @Test
    fun `addTrack should keep track in list when repository fails`() = runTest {

        val track = Track(
            id = 0,
            name = "Test Track",
            duration = "3:45"
        )

        val exception = Exception("Network error")
        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } returns Result.failure(exception)

        val initialTracksCount = viewModel.uiState.value.tracks.size


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))
        advanceUntilIdle()


        val updatedUiState = viewModel.uiState.value
        assertEquals(initialTracksCount + 1, updatedUiState.tracks.size)
        assertTrue(updatedUiState.tracks.any { it.name == "Test Track" })
        assertNull(updatedUiState.error)
    }

    @Test
    fun `addTrack should generate sequential IDs for temporary tracks`() = runTest {

        val track1 = Track(id = 0, name = "Track 1", duration = "3:00")
        val track2 = Track(id = 0, name = "Track 2", duration = "4:00")

        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } returns Result.failure(Exception("Network error"))


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track1))
        viewModel.onEvent(AlbumDetailEvent.AddTrack(track2))
        advanceUntilIdle()


        val tracks = viewModel.uiState.value.tracks
        val track1InList = tracks.find { it.name == "Track 1" }
        val track2InList = tracks.find { it.name == "Track 2" }

        assertNotNull(track1InList)
        assertNotNull(track2InList)
        assertNotEquals(track1InList?.id, track2InList?.id)
    }

    @Test
    fun `addTrack should handle multiple tracks being added quickly`() = runTest {

        val tracks = listOf(
            Track(id = 0, name = "Track 1", duration = "3:00"),
            Track(id = 0, name = "Track 2", duration = "4:00"),
            Track(id = 0, name = "Track 3", duration = "5:00")
        )

        var callCount = 0
        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } coAnswers {
            callCount++
            Result.success(Track(
                id = 900 + callCount,
                name = secondArg<Track>().name,
                duration = secondArg<Track>().duration
            ))
        }

        val initialCount = viewModel.uiState.value.tracks.size


        tracks.forEach { track ->
            viewModel.onEvent(AlbumDetailEvent.AddTrack(track))
        }
        advanceUntilIdle()


        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCount + 3, updatedUiState.tracks.size)
        assertTrue(updatedUiState.tracks.any { it.name == "Track 1" })
        assertTrue(updatedUiState.tracks.any { it.name == "Track 2" })
        assertTrue(updatedUiState.tracks.any { it.name == "Track 3" })
    }

    @Test
    fun `addTrack should not set loading state`() = runTest {

        val track = Track(id = 0, name = "Test Track", duration = "3:45")

        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } coAnswers {
            kotlinx.coroutines.delay(50)
            Result.success(Track(id = 999, name = "Test Track", duration = "3:45"))
        }


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))


        assertFalse(viewModel.uiState.value.isLoading)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `addTrack should use correct albumId from current state`() = runTest {

        val albumId = 123
        val album = createSampleAlbum()
        val track = Track(id = 0, name = "Test Track", duration = "3:45")

        coEvery { albumRepository.getAlbumById(albumId) } returns Result.success(album)
        coEvery { albumRepository.addTrackToAlbum(any(), any()) } returns Result.success(track)


        viewModel.loadAlbumById(albumId)
        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))
        advanceUntilIdle()


        coVerify {
            albumRepository.addTrackToAlbum(
                albumId = albumId,
                track = track
            )
        }
    }

    @Test
    fun `addTrack should trim track name`() = runTest {

        val trackWithSpaces = Track(
            id = 0,
            name = "  Test Track  ",
            duration = "3:45"
        )

        val savedTrack = Track(
            id = 999,
            name = "Test Track",
            duration = "3:45"
        )

        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } returns Result.success(savedTrack)


        viewModel.onEvent(AlbumDetailEvent.AddTrack(trackWithSpaces))
        advanceUntilIdle()


        val tracks = viewModel.uiState.value.tracks
        assertFalse(tracks.any { it.name.startsWith(" ") || it.name.endsWith(" ") })
    }

    @Test
    fun `addTrack should handle exception during API call`() = runTest {

        val track = Track(id = 0, name = "Test Track", duration = "3:45")

        coEvery {
            albumRepository.addTrackToAlbum(any(), any())
        } throws RuntimeException("Unexpected error")

        val initialCount = viewModel.uiState.value.tracks.size


        viewModel.onEvent(AlbumDetailEvent.AddTrack(track))
        advanceUntilIdle()


        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCount + 1, updatedUiState.tracks.size)
        assertNull(updatedUiState.error)
    }


}
