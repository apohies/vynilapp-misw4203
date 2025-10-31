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
        val initialCommentsCount = viewModel.uiState.value.comments.size

        
        viewModel.onEvent(AlbumDetailEvent.AddComment(commentText))

        
        val updatedUiState = viewModel.uiState.value
        assertEquals(initialCommentsCount + 1, updatedUiState.comments.size)
        assertTrue(updatedUiState.comments.any { it.description == commentText })
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
}
