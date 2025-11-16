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
}
