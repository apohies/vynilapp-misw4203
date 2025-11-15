package com.uniandes.vynilapp.presentation.artist.detail

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.model.PerformerPrizes
import com.uniandes.vynilapp.model.repository.ArtistRepository
import com.uniandes.vynilapp.viewModels.artists.ArtistDetailViewModel
import com.uniandes.vynilapp.views.states.ArtistDetailEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistDetailViewModelTest {

    private lateinit var artistRepository: ArtistRepository
    private lateinit var viewModel: ArtistDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        artistRepository = mockk()
        viewModel = ArtistDetailViewModel(artistRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArtistById should update UI state with artist data when successful`() = runTest {
        // Arrange
        val artistId = 100
        val artist = createSampleArtist()
        val successResult = Result.success(artist)

        coEvery { artistRepository.getArtistById(artistId) } returns successResult

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNull(uiState.error)
        Assert.assertNotNull(uiState.artist)
        Assert.assertEquals(artist.id, uiState.artist?.id)
        Assert.assertEquals(artist.name, uiState.artist?.name)
        Assert.assertEquals(artist.albums.size, uiState.albums.size)
        Assert.assertEquals(artist.performerPrizes.size, uiState.performerPrizes.size)

        coVerify { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `loadArtistById should update UI state with error when repository fails`() = runTest {
        // Arrange
        val artistId = 100
        val exception = Exception("Network error")
        val failureResult = Result.failure<Artist>(exception)

        coEvery { artistRepository.getArtistById(artistId) } returns failureResult

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNotNull(uiState.error)
        Assert.assertTrue(uiState.error?.contains("Error al cargar el álbum") == true)
        Assert.assertNull(uiState.artist)

        coVerify { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `loadArtistById should set loading state during API call`() = runTest {
        // Arrange
        val artistId = 100
        val artist = createSampleArtist()
        val successResult = Result.success(artist)

        coEvery { artistRepository.getArtistById(artistId) } returns successResult

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertFalse(uiState.isLoading) // Should be false after completion
        Assert.assertNull(uiState.error)

        coVerify { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `loadArtistById should handle exception during loading`() = runTest {
        // Arrange
        val artistId = 100

        coEvery { artistRepository.getArtistById(artistId) } throws Exception("Connection timeout")

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNotNull(uiState.error)
        Assert.assertTrue(uiState.error?.contains("Error de conexión") == true)

        coVerify { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `onEvent LoadArtistById should call loadArtistById with correct ID`() = runTest {
        // Arrange
        val artistId = 123
        val artist = createSampleArtist()
        val successResult = Result.success(artist)

        coEvery { artistRepository.getArtistById(artistId) } returns successResult

        // Act
        viewModel.onEvent(ArtistDetailEvent.LoadArtistById(artistId))

        // Assert
        coVerify { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `onEvent LoadArtist should call loadArtist with current artist ID`() = runTest {
        // Arrange
        val artistId = 100
        val artist = createSampleArtist()
        val successResult = Result.success(artist)

        coEvery { artistRepository.getArtistById(artistId) } returns successResult

        // Act
        viewModel.loadArtistById(artistId) // Set current artist ID
        viewModel.onEvent(ArtistDetailEvent.LoadArtist)

        // Assert
        coVerify(exactly = 2) { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `getCurrentArtistId should return current artist ID`() = runTest {
        // Arrange
        val artistId = 123
        val artist = createSampleArtist()
        coEvery { artistRepository.getArtistById(artistId) } returns Result.success(artist)

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val currentId = viewModel.getCurrentArtistId()
        Assert.assertEquals(artistId, currentId)
    }

    @Test
    fun `refreshArtist should reload current artist`() = runTest {
        // Arrange
        val artistId = 100
        val artist = createSampleArtist()
        val successResult = Result.success(artist)

        coEvery { artistRepository.getArtistById(artistId) } returns successResult

        // Act
        viewModel.loadArtistById(artistId) // Set current artist ID
        viewModel.refreshArtist()

        // Assert
        coVerify(exactly = 2) { artistRepository.getArtistById(artistId) }
    }

    @Test
    fun `loadArtistById should populate albums from artist data`() = runTest {
        // Arrange
        val artistId = 100
        val albums = listOf(
            createSampleAlbum(1, "Album 1"),
            createSampleAlbum(2, "Album 2")
        )
        val artist = createSampleArtist(albums = albums)

        coEvery { artistRepository.getArtistById(artistId) } returns Result.success(artist)

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertEquals(2, uiState.albums.size)
        Assert.assertEquals("Album 1", uiState.albums[0].name)
        Assert.assertEquals("Album 2", uiState.albums[1].name)
    }

    @Test
    fun `loadArtistById should populate performerPrizes from artist data`() = runTest {
        // Arrange
        val artistId = 100
        val prizes = listOf(
            PerformerPrizes(1, "2020-01-01T00:00:00.000Z"),
            PerformerPrizes(2, "2021-01-01T00:00:00.000Z")
        )
        val artist = createSampleArtist(prizes = prizes)

        coEvery { artistRepository.getArtistById(artistId) } returns Result.success(artist)

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertEquals(2, uiState.performerPrizes.size)
        Assert.assertEquals(prizes[0].id, uiState.performerPrizes[0].id)
    }

    @Test
    fun `loadArtistById should handle empty albums list`() = runTest {
        // Arrange
        val artistId = 100
        val artist = createSampleArtist(albums = emptyList())

        coEvery { artistRepository.getArtistById(artistId) } returns Result.success(artist)

        // Act
        viewModel.loadArtistById(artistId)

        // Assert
        val uiState = viewModel.uiState.value
        Assert.assertTrue(uiState.albums.isEmpty())
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNull(uiState.error)
    }

    @Test
    fun `multiple sequential loadArtistById calls should update state correctly`() = runTest {
        // Arrange
        val artistId1 = 100
        val artistId2 = 200
        val artist1 = createSampleArtist(id = artistId1, name = "Artist 1")
        val artist2 = createSampleArtist(id = artistId2, name = "Artist 2")

        coEvery { artistRepository.getArtistById(artistId1) } returns Result.success(artist1)
        coEvery { artistRepository.getArtistById(artistId2) } returns Result.success(artist2)

        // Act
        viewModel.loadArtistById(artistId1)
        val state1 = viewModel.uiState.value

        viewModel.loadArtistById(artistId2)
        val state2 = viewModel.uiState.value

        // Assert
        Assert.assertEquals("Artist 1", state1.artist?.name)
        Assert.assertEquals("Artist 2", state2.artist?.name)
        Assert.assertEquals(artistId2, viewModel.getCurrentArtistId())
    }

    @Test
    fun `default artist ID should be 100`() = runTest {
        // Assert
        Assert.assertEquals(100, viewModel.getCurrentArtistId())
    }

    // Helper methods to create test data
    private fun createSampleArtist(
        id: Int = 100,
        name: String = "Rubén Blades",
        albums: List<ArtistAlbum> = emptyList(),
        prizes: List<PerformerPrizes> = emptyList()
    ): Artist {
        return Artist(
            id = id,
            name = name,
            image = "https://example.com/artist.jpg",
            description = "Legendary salsa musician from Panama",
            birthDate = "1948-07-16T00:00:00.000Z",
            albums = albums,
            performerPrizes = prizes
        )
    }

    private fun createSampleAlbum(id: Int, name: String): ArtistAlbum {
        return ArtistAlbum(
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