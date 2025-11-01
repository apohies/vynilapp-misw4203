package com.uniandes.vynilapp.presentation.artist

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.model.PerformerPrizes
import com.uniandes.vynilapp.model.repository.ArtistRepository
import com.uniandes.vynilapp.viewModels.artists.ArtistsViewModel
import com.uniandes.vynilapp.viewModels.artists.ArtistUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistViewModelTest {

    private lateinit var artistRepository: ArtistRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        artistRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init or loadArtists should update uiState with artists on success`() = runTest {
        // Arrange
        val artists = listOf(createSampleArtist(1, "Brad Pitt"), createSampleArtist(2, "Tom Cruise"))
        coEvery { artistRepository.getAllArtists() } returns Result.success(artists)

        // Act
        val viewModel = ArtistsViewModel(artistRepository)
        advanceUntilIdle() // ensure coroutines complete

        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is ArtistUiState.Success)
        val success = uiState as ArtistUiState.Success
        assertEquals(2, success.artists.size)
        assertEquals(2, viewModel.filteredArtists.value.size)
        coVerify { artistRepository.getAllArtists() }
    }

    @Test
    fun `loadArtists should set error uiState when repository fails`() = runTest {
        // Arrange
        val errorMsg = "Network failure"
        coEvery { artistRepository.getAllArtists() } returns Result.failure(Exception(errorMsg))

        // Act
        val viewModel = ArtistsViewModel(artistRepository)
        advanceUntilIdle()

        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState is ArtistUiState.Error)
        val error = uiState as ArtistUiState.Error
        assertTrue(error.message.contains("Error") || error.message.isNotEmpty())
        coVerify { artistRepository.getAllArtists() }
    }

    @Test
    fun `setSearchQuery should filter artists case insensitively`() = runTest {
        // Arrange
        val artists = listOf(
            createSampleArtist(1, "Tom Cruise"),
            createSampleArtist(2, "Brad pitt"),
            createSampleArtist(3, "tom Holland")
        )
        coEvery { artistRepository.getAllArtists() } returns Result.success(artists)

        // Act
        val viewModel = ArtistsViewModel(artistRepository)
        advanceUntilIdle()

        // initial all
        assertEquals(3, viewModel.filteredArtists.value.size)

        viewModel.setSearchQuery("Tom")
        advanceUntilIdle()

        // Assert
        val filtered = viewModel.filteredArtists.value
        assertEquals(2, filtered.size)
        assertTrue(filtered.any { it.name == "Tom Cruise" })
        assertTrue(filtered.any { it.name == "tom Holland" })
    }

    @Test
    fun `empty search query should return full list`() = runTest {
        // Arrange
        val artists = listOf(createSampleArtist(1, "A"), createSampleArtist(2, "B"))
        coEvery { artistRepository.getAllArtists() } returns Result.success(artists)

        // Act
        val viewModel = ArtistsViewModel(artistRepository)
        advanceUntilIdle()

        viewModel.setSearchQuery("") // empty
        advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.filteredArtists.value.size)
    }

    @Test
    fun `multiple loadArtists calls should update uiState and list`() = runTest {
        // Arrange
        val artists1 = listOf(createSampleArtist(1, "Joaquin Phoenix"))
        val artists2 = listOf(createSampleArtist(2, "Hans Zimmer"))
        coEvery { artistRepository.getAllArtists() } returnsMany listOf(Result.success(artists1), Result.success(artists2))

        // Act
        val viewModel = ArtistsViewModel(artistRepository)
        advanceUntilIdle()
        val firstState = viewModel.uiState.value
        assertTrue(firstState is ArtistUiState.Success)
        assertEquals(1, (firstState as ArtistUiState.Success).artists.size)

        // Trigger load again (will return second result)
        viewModel.loadArtists()
        advanceUntilIdle()
        val secondState = viewModel.uiState.value
        assertTrue(secondState is ArtistUiState.Success)
        assertEquals(1, (secondState as ArtistUiState.Success).artists.size)
        assertEquals("Hans Zimmer", (secondState as ArtistUiState.Success).artists[0].name)

        coVerify(exactly = 2) { artistRepository.getAllArtists() }
    }

    // Helper
    private fun createSampleArtist(
        id: Int = 1,
        name: String = "Artist",
        albums: List<ArtistAlbum> = emptyList(),
        prizes: List<PerformerPrizes> = emptyList()
    ): Artist {
        return Artist(
            id = id,
            name = name,
            image = "https://example.com/image.jpg",
            description = "desc",
            birthDate = "2000-01-01T00:00:00.000Z",
            albums = albums,
            performerPrizes = prizes
        )
    }
}