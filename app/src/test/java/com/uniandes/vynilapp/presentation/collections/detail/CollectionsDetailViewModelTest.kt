package com.uniandes.vynilapp.presentation.collections.detail

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.CollectorAlbum
import com.uniandes.vynilapp.model.repository.CollectorRepository
import com.uniandes.vynilapp.viewModels.collections.CollectionsDetailViewModel
import com.uniandes.vynilapp.views.states.CollectorDetailEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionsDetailViewModelTest {

    private lateinit var collectorRepository: CollectorRepository
    private lateinit var viewModel: CollectionsDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        collectorRepository = mockk()
        viewModel = CollectionsDetailViewModel(collectorRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCollectorById should update UI state with collector data when successful`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(
            createSampleCollectorAlbum(1, 35.0, "Active"),
            createSampleCollectorAlbum(2, 50.0, "Active")
        )
        val successCollectorResult = Result.success(collector)
        val successAlbumsResult = Result.success(collectorAlbums)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns successCollectorResult
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns successAlbumsResult

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
        assertNotNull(uiState.collector)
        assertEquals(collector.id, uiState.collector?.id)
        assertEquals(collector.name, uiState.collector?.name)
        assertEquals(2, uiState.albums.size)

        coVerify { collectorRepository.getCollectorById(collectorId) }
        coVerify { collectorRepository.getCollectorAlbums(collectorId) }
    }

    @Test
    fun `loadCollectorById should update UI state with error when collector fetch fails`() = runTest {
        // Arrange
        val collectorId = 100
        val exception = Exception("Network error")
        val failureResult = Result.failure<Collector>(exception)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns failureResult

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.error)
        assertTrue(uiState.error?.contains("Error al cargar el coleccionista") == true)
        assertNull(uiState.collector)

        coVerify { collectorRepository.getCollectorById(collectorId) }
        coVerify(exactly = 0) { collectorRepository.getCollectorAlbums(any()) }
    }

    @Test
    fun `loadCollectorById should update UI state with error when albums fetch fails`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val exception = Exception("Failed to load albums")
        val successCollectorResult = Result.success(collector)
        val failureAlbumsResult = Result.failure<List<CollectorAlbum>>(exception)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns successCollectorResult
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns failureAlbumsResult

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.error)
        assertTrue(uiState.error?.contains("Error al cargar los albums") == true)

        coVerify { collectorRepository.getCollectorById(collectorId) }
        coVerify { collectorRepository.getCollectorAlbums(collectorId) }
    }

    @Test
    fun `loadCollectorById should set loading state during API call`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))
        val successCollectorResult = Result.success(collector)
        val successAlbumsResult = Result.success(collectorAlbums)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns successCollectorResult
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns successAlbumsResult

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading) // Should be false after completion
        assertNull(uiState.error)

        coVerify { collectorRepository.getCollectorById(collectorId) }
        coVerify { collectorRepository.getCollectorAlbums(collectorId) }
    }

    @Test
    fun `loadCollectorById should handle exception during loading`() = runTest {
        // Arrange
        val collectorId = 100

        coEvery { collectorRepository.getCollectorById(collectorId) } throws Exception("Connection timeout")

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.error)
        assertTrue(uiState.error?.contains("Error de conexión") == true)

        coVerify { collectorRepository.getCollectorById(collectorId) }
    }

    @Test
    fun `onEvent LoadCollectorById should call loadCollectorById with correct ID`() = runTest {
        // Arrange
        val collectorId = 123
        val collector = createSampleCollector()
        val collectorAlbums = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))
        val successCollectorResult = Result.success(collector)
        val successAlbumsResult = Result.success(collectorAlbums)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns successCollectorResult
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns successAlbumsResult

        // Act
        viewModel.onEvent(CollectorDetailEvent.LoadCollectorById(collectorId))

        // Assert
        coVerify { collectorRepository.getCollectorById(collectorId) }
        coVerify { collectorRepository.getCollectorAlbums(collectorId) }
    }

    @Test
    fun `onEvent LoadCollector should call loadCollector with current collector ID`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))
        val successCollectorResult = Result.success(collector)
        val successAlbumsResult = Result.success(collectorAlbums)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns successCollectorResult
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns successAlbumsResult

        // Act
        viewModel.loadCollectorById(collectorId) // Set current collector ID
        viewModel.onEvent(CollectorDetailEvent.LoadCollector)

        // Assert
        coVerify(exactly = 2) { collectorRepository.getCollectorById(collectorId) }
        coVerify(exactly = 2) { collectorRepository.getCollectorAlbums(collectorId) }
    }

    @Test
    fun `getCurrentCollectorId should return current collector ID`() = runTest {
        // Arrange
        val collectorId = 123
        val collector = createSampleCollector()
        val collectorAlbums = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))

        coEvery { collectorRepository.getCollectorById(collectorId) } returns Result.success(collector)
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns Result.success(collectorAlbums)

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val currentId = viewModel.getCurrentCollectorId()
        assertEquals(collectorId, currentId)
    }

    @Test
    fun `refreshCollector should reload current collector`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))
        val successCollectorResult = Result.success(collector)
        val successAlbumsResult = Result.success(collectorAlbums)

        coEvery { collectorRepository.getCollectorById(collectorId) } returns successCollectorResult
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns successAlbumsResult

        // Act
        viewModel.loadCollectorById(collectorId) // Set current collector ID
        viewModel.refreshCollector()

        // Assert
        coVerify(exactly = 2) { collectorRepository.getCollectorById(collectorId) }
        coVerify(exactly = 2) { collectorRepository.getCollectorAlbums(collectorId) }
    }

    @Test
    fun `loadCollectorById should populate albums with correct data`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(
            createSampleCollectorAlbum(1, 35.0, "Active"),
            createSampleCollectorAlbum(2, 50.0, "Inactive"),
            createSampleCollectorAlbum(3, 25.0, "Active")
        )

        coEvery { collectorRepository.getCollectorById(collectorId) } returns Result.success(collector)
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns Result.success(collectorAlbums)

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertEquals(3, uiState.albums.size)
        assertEquals(35.0, uiState.albums[0].price, 0.01)
        assertEquals("Active", uiState.albums[0].status)
        assertEquals(50.0, uiState.albums[1].price, 0.01)
        assertEquals("Inactive", uiState.albums[1].status)
    }

    @Test
    fun `loadCollectorById should handle empty albums list`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val emptyAlbums = emptyList<CollectorAlbum>()

        coEvery { collectorRepository.getCollectorById(collectorId) } returns Result.success(collector)
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns Result.success(emptyAlbums)

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertTrue(uiState.albums.isEmpty())
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
        assertNotNull(uiState.collector)
    }

    @Test
    fun `multiple sequential loadCollectorById calls should update state correctly`() = runTest {
        // Arrange
        val collectorId1 = 100
        val collectorId2 = 200
        val collector1 = createSampleCollector(id = collectorId1, name = "Collector 1")
        val collector2 = createSampleCollector(id = collectorId2, name = "Collector 2")
        val albums1 = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))
        val albums2 = listOf(createSampleCollectorAlbum(2, 50.0, "Active"))

        coEvery { collectorRepository.getCollectorById(collectorId1) } returns Result.success(collector1)
        coEvery { collectorRepository.getCollectorAlbums(collectorId1) } returns Result.success(albums1)
        coEvery { collectorRepository.getCollectorById(collectorId2) } returns Result.success(collector2)
        coEvery { collectorRepository.getCollectorAlbums(collectorId2) } returns Result.success(albums2)

        // Act
        viewModel.loadCollectorById(collectorId1)
        val state1 = viewModel.uiState.value

        viewModel.loadCollectorById(collectorId2)
        val state2 = viewModel.uiState.value

        // Assert
        assertEquals("Collector 1", state1.collector?.name)
        assertEquals("Collector 2", state2.collector?.name)
        assertEquals(collectorId2, viewModel.getCurrentCollectorId())
    }

    @Test
    fun `default collector ID should be 100`() = runTest {
        // Assert
        assertEquals(100, viewModel.getCurrentCollectorId())
    }

    @Test
    fun `loadCollectorById should clear previous error on new load`() = runTest {
        // Arrange
        val collectorId1 = 100
        val collectorId2 = 200
        val collector2 = createSampleCollector(id = collectorId2)
        val albums2 = listOf(createSampleCollectorAlbum(1, 35.0, "Active"))

        // First call fails
        coEvery { collectorRepository.getCollectorById(collectorId1) } returns Result.failure(Exception("Error"))

        // Second call succeeds
        coEvery { collectorRepository.getCollectorById(collectorId2) } returns Result.success(collector2)
        coEvery { collectorRepository.getCollectorAlbums(collectorId2) } returns Result.success(albums2)

        // Act
        viewModel.loadCollectorById(collectorId1)
        val state1 = viewModel.uiState.value

        viewModel.loadCollectorById(collectorId2)
        val state2 = viewModel.uiState.value

        // Assert
        assertNotNull(state1.error)
        assertNull(state2.error)
        assertNotNull(state2.collector)
    }

    @Test
    fun `loadCollectorById should handle albums with different statuses`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(
            createSampleCollectorAlbum(1, 35.0, "Active"),
            createSampleCollectorAlbum(2, 50.0, "Inactive"),
            createSampleCollectorAlbum(3, 25.0, "Pending")
        )

        coEvery { collectorRepository.getCollectorById(collectorId) } returns Result.success(collector)
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns Result.success(collectorAlbums)

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertEquals(3, uiState.albums.size)
        assertEquals("Active", uiState.albums[0].status)
        assertEquals("Inactive", uiState.albums[1].status)
        assertEquals("Pending", uiState.albums[2].status)
    }

    @Test
    fun `loadCollectorById should handle albums with different prices`() = runTest {
        // Arrange
        val collectorId = 100
        val collector = createSampleCollector()
        val collectorAlbums = listOf(
            createSampleCollectorAlbum(1, 10.50, "Active"),
            createSampleCollectorAlbum(2, 99.99, "Active"),
            createSampleCollectorAlbum(3, 0.0, "Active")
        )

        coEvery { collectorRepository.getCollectorById(collectorId) } returns Result.success(collector)
        coEvery { collectorRepository.getCollectorAlbums(collectorId) } returns Result.success(collectorAlbums)

        // Act
        viewModel.loadCollectorById(collectorId)

        // Assert
        val uiState = viewModel.uiState.value
        assertEquals(10.50, uiState.albums[0].price, 0.01)
        assertEquals(99.99, uiState.albums[1].price, 0.01)
        assertEquals(0.0, uiState.albums[2].price, 0.01)
    }

    // Helper methods to create test data
    private fun createSampleCollector(
        id: Int = 1,
        name: String = "Jaime Andrés",
        imageUrl: String = "https://i.pravatar.cc/150",
        albumCount: Int = 3
    ): Collector {
        return Collector(
            id = id,
            name = name,
            imageUrl = imageUrl,
            albumCount = albumCount
        )
    }

    private fun createSampleCollectorAlbum(
        id: Int,
        price: Double,
        status: String
    ): CollectorAlbum {
        return CollectorAlbum(
            id = id,
            price = price,
            status = status,
            album = Album(
                id = id,
                name = "Album $id",
                cover = "https://example.com/album$id.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Test album description",
                genre = "Salsa",
                recordLabel = "Test Label"
            )
        )
    }
}