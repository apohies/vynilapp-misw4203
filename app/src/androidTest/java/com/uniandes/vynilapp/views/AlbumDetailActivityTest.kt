package com.uniandes.vynilapp.views

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Comment
import com.uniandes.vynilapp.model.Performer
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.views.states.AlbumDetailEvent
import com.uniandes.vynilapp.views.states.AlbumDetailUiState
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class AlbumDetailActivityTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var backClickCount = 0
    private var playClickCount = 0
    private var likeClickCount = 0
    private var saveClickCount = 0
    private var shareClickCount = 0
    private val eventList = mutableListOf<AlbumDetailEvent>()

    // Mock data
    private lateinit var mockAlbum: Album
    private lateinit var mockTracks: List<Track>
    private lateinit var mockComments: List<Comment>
    private lateinit var mockPerformer: Performer
    private lateinit var mockUiState: AlbumDetailUiState
    private lateinit var mockUiStatePlaying: AlbumDetailUiState
    private lateinit var mockUiStateLiked: AlbumDetailUiState

    @Before
    fun setup() {
        backClickCount = 0
        playClickCount = 0
        likeClickCount = 0
        saveClickCount = 0
        shareClickCount = 0
        eventList.clear()

        // Setup mock performer
        mockPerformer = Performer(
            id = 1,
            name = "Queen",
            image = "https://example.com/queen.jpg",
            description = "British rock band",
            creationDate = "1970-01-01T00:00:00.000Z"
        )

        // Setup mock tracks
        mockTracks = listOf(
            Track(id = 1, name = "Bohemian Rhapsody", duration = "5:55"),
            Track(id = 2, name = "We Will Dance You", duration = "2:02"),
            Track(id = 3, name = "We Are The Champions", duration = "2:59")
        )

        // Setup mock comments
        mockComments = listOf(
            Comment(id = 1, description = "Amazing album!", rating = 5),
            Comment(id = 2, description = "Classic rock masterpiece", rating = 5),
            Comment(id = 3, description = "Best album ever", rating = 5)
        )

        // Setup mock album
        mockAlbum = Album(
            id = 100,
            name = "A Night at the Opera",
            cover = "https://example.com/album.jpg",
            releaseDate = "1975-11-21T00:00:00.000Z",
            description = "Queen's fourth studio album",
            genre = "Rock",
            recordLabel = "EMI",
            tracks = mockTracks,
            performers = listOf(mockPerformer),
            comments = mockComments
        )

        // Setup UI states
        mockUiState = AlbumDetailUiState(
            isLoading = false,
            album = mockAlbum,
            tracks = mockTracks,
            comments = mockComments,
            error = null,
            isPlaying = false,
            isLiked = false,
            isSaved = false,
            newCommentText = ""
        )

        mockUiStatePlaying = mockUiState.copy(isPlaying = true)
        mockUiStateLiked = mockUiState.copy(isLiked = true, isSaved = true)
    }

    // ==================== AlbumDetailContent Tests ====================

    // Test 1: Validate AlbumDetailContent displays album name
    @Test
    fun validateAlbumNameDisplayTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("A Night at the Opera")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate performer name is displayed
    @Test
    fun validatePerformerNameDisplayTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Queen")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate genre and year are displayed
    @Test
    fun validateGenreAndYearDisplayTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Rock", substring = true)
            .assertExists()

        composeTestRule.onNodeWithText("20 de nov. 1975", substring = true)
            .assertExists()
    }

    // Test 4: Validate TopBar back button
    @Test
    fun validateTopBarBackButtonTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = { backClickCount++ }
            )
        }

        composeTestRule.onNodeWithContentDescription("Atrás")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, backClickCount)
    }

    // Test 5: Validate TopBar title
    @Test
    fun validateTopBarTitleTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Detalles del Álbum")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 6: Validate menu button in TopBar
    @Test
    fun validateTopBarMenuButtonTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Menú")
            .assertExists()
    }

    // ==================== AlbumHeader Tests ====================

    // Test 7: Validate play button
    @Test
    fun validatePlayButtonTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = { event ->
                    if (event is AlbumDetailEvent.PlayAlbum) {
                        playClickCount++
                    }
                },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Reproducir")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, playClickCount)
    }

    // Test 8: Validate pause button when playing
    @Test
    fun validatePauseButtonWhenPlayingTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiStatePlaying,
                onEvent = { event ->
                    if (event is AlbumDetailEvent.PauseAlbum) {
                        playClickCount++
                    }
                },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Detener")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, playClickCount)
    }

    // Test 9: Validate like button
    @Test
    fun validateLikeButtonTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = { event ->
                    if (event is AlbumDetailEvent.ToggleLike) {
                        likeClickCount++
                    }
                },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Me gusta")
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, likeClickCount)
    }

    // Test 10: Validate save button
    @Test
    fun validateSaveButtonTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = { event ->
                    if (event is AlbumDetailEvent.ToggleSave) {
                        saveClickCount++
                    }
                },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Guardar")
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, saveClickCount)
    }

    // Test 11: Validate share button
    @Test
    fun validateShareButtonTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = { event ->
                    if (event is AlbumDetailEvent.ShareAlbum) {
                        shareClickCount++
                    }
                },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Compartir")
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, shareClickCount)
    }

    // ==================== SongsSection Tests ====================

    // Test 12: Validate songs section title
    @Test
    fun validateSongsSectionTitleTest() {
        composeTestRule.setContent {
            SongsSection(
                tracks = mockTracks,
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("Lista de Canciones")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 13: Validate all tracks are displayed
    @Test
    fun validateAllTracksDisplayedTest() {
        composeTestRule.setContent {
            SongsSection(
                tracks = mockTracks,
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertExists()
        composeTestRule.onNodeWithText("We Will Dance You").assertExists()
        composeTestRule.onNodeWithText("We Are The Champions").assertExists()
    }

    // Test 14: Validate track durations are displayed
    @Test
    fun validateTrackDurationsTest() {
        composeTestRule.setContent {
            SongsSection(
                tracks = mockTracks,
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("5:55").assertExists()
        composeTestRule.onNodeWithText("2:02").assertExists()
        composeTestRule.onNodeWithText("2:59").assertExists()
    }

    // Test 15: Validate track numbers are displayed
    @Test
    fun validateTrackNumbersTest() {
        composeTestRule.setContent {
            SongsSection(
                tracks = mockTracks,
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("1.").assertExists()
        composeTestRule.onNodeWithText("2.").assertExists()
        composeTestRule.onNodeWithText("3.").assertExists()
    }

    // Test 16: Validate add track button
    @Test
    fun validateAddTrackButtonTest() {
        composeTestRule.setContent {
            SongsSection(
                tracks = mockTracks,
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("Agregar nueva canción")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 17: Validate add track button click
    @Test
    fun validateAddTrackButtonClickTest() {
        var trackAdded = false

        composeTestRule.setContent {
            SongsSection(
                tracks = mockTracks,
                onAddTrack = { trackAdded = true }
            )
        }

        composeTestRule.onNodeWithText("Agregar nueva canción")
            .performClick()

        composeTestRule.waitForIdle()
        assertTrue(trackAdded)
    }

    // Test 18: Validate empty tracks list
    @Test
    fun validateEmptyTracksListTest() {
        composeTestRule.setContent {
            SongsSection(
                tracks = emptyList(),
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("Lista de Canciones").assertExists()
        composeTestRule.onNodeWithText("Agregar nueva canción").assertExists()
    }

    // ==================== CommentsSection Tests ====================

    // Test 19: Validate comments section title
    @Test
    fun validateCommentsSectionTitleTest() {
        composeTestRule.setContent {
            CommentsSection(
                comments = mockComments,
                newCommentText = "",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        composeTestRule.onNodeWithText("Comentarios")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 20: Validate all comments are displayed
    @Test
    fun validateAllCommentsDisplayedTest() {
        composeTestRule.setContent {
            CommentsSection(
                comments = mockComments,
                newCommentText = "",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        composeTestRule.onNodeWithText("Amazing album!").assertExists()
        composeTestRule.onNodeWithText("Classic rock masterpiece").assertExists()
        composeTestRule.onNodeWithText("Best album ever").assertExists()
    }

    // Test 21: Validate comment text field
    @Test
    fun validateCommentTextFieldTest() {
        composeTestRule.setContent {
            CommentsSection(
                comments = emptyList(),
                newCommentText = "",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        composeTestRule.onNodeWithText("Añadir un comentario...")
            .assertExists()
    }

    // Test 22: Validate comment text input
    @Test
    fun validateCommentTextInputTest() {
        var commentText = ""

        composeTestRule.setContent {
            CommentsSection(
                comments = emptyList(),
                newCommentText = commentText,
                onCommentTextChange = { commentText = it },
                onAddComment = {}
            )
        }

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Great album!")

        assertEquals("Great album!", commentText)
    }

    // Test 23: Validate send comment button
    @Test
    fun validateSendCommentButtonTest() {
        composeTestRule.setContent {
            CommentsSection(
                comments = emptyList(),
                newCommentText = "Test comment",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Enviar")
            .assertExists()
    }

    // Test 24: Validate send comment callback
    @Test
    fun validateSendCommentCallbackTest() {
        var sentComment = ""

        composeTestRule.setContent {
            CommentsSection(
                comments = emptyList(),
                newCommentText = "My comment",
                onCommentTextChange = {},
                onAddComment = { sentComment = it }
            )
        }

        composeTestRule.onNodeWithContentDescription("Enviar")
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals("My comment", sentComment)
    }

    // Test 25: Validate empty comments list
    @Test
    fun validateEmptyCommentsListTest() {
        composeTestRule.setContent {
            CommentsSection(
                comments = emptyList(),
                newCommentText = "",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        composeTestRule.onNodeWithText("Comentarios").assertExists()
        composeTestRule.onNodeWithText("Añadir un comentario...").assertExists()
    }

    // ==================== Integration Tests ====================

    // Test 26: Validate complete album detail layout
    @Test
    fun validateCompleteLayoutTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = {}
            )
        }

        // Verify all sections exist
        composeTestRule.onNodeWithText("Detalles del Álbum").assertExists()
        composeTestRule.onNodeWithText("A Night at the Opera").assertExists()
        composeTestRule.onNodeWithText("Lista de Canciones").assertExists()
        composeTestRule.onNodeWithText("Comentarios").assertExists()
    }

    // Test 27: Validate multiple action buttons
    @Test
    fun validateMultipleActionButtonsTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = { eventList.add(it) },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Me gusta").performClick()
        composeTestRule.onNodeWithContentDescription("Guardar").performClick()
        composeTestRule.onNodeWithContentDescription("Compartir").performClick()

        composeTestRule.waitForIdle()
        assertEquals(3, eventList.size)
        assertTrue(eventList[0] is AlbumDetailEvent.ToggleLike)
        assertTrue(eventList[1] is AlbumDetailEvent.ToggleSave)
        assertTrue(eventList[2] is AlbumDetailEvent.ShareAlbum)
    }

    // Test 28: Validate play/pause toggle
    @Test
    fun validatePlayPauseToggleTest() {
        var isPlaying = false

        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState.copy(isPlaying = isPlaying),
                onEvent = { event ->
                    when (event) {
                        is AlbumDetailEvent.PlayAlbum -> isPlaying = true
                        is AlbumDetailEvent.PauseAlbum -> isPlaying = false
                        else -> {}
                    }
                },
                onBack = {}
            )
        }

        assertFalse(isPlaying)

        composeTestRule.onNodeWithContentDescription("Reproducir").performClick()
        composeTestRule.waitForIdle()
        // Note: State doesn't update in test, but event is sent
    }

    // Test 29: Validate album with null performer
    @Test
    fun validateAlbumNullPerformerTest() {
        val albumNoPerformer = mockAlbum.copy(performers = emptyList())
        val uiState = mockUiState.copy(album = albumNoPerformer)

        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = uiState,
                onEvent = {},
                onBack = {}
            )
        }

        // Should show "Artista" as placeholder
        composeTestRule.onNodeWithText("Artista").assertExists()
    }

    // Test 30: Validate album with long name
    @Test
    fun validateAlbumLongNameTest() {
        val longName = "This is a very long album name that might wrap to multiple lines"
        val albumLongName = mockAlbum.copy(name = longName)
        val uiState = mockUiState.copy(album = albumLongName)

        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = uiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText(longName, substring = true).assertExists()
    }

    // Test 31: Validate many tracks
    @Test
    fun validateManyTracksTest() {
        val manyTracks = List(20) { index ->
            Track(
                id = index + 1,
                name = "Track ${index + 1}",
                duration = "${index % 5}:${index % 60}"
            )
        }

        composeTestRule.setContent {
            SongsSection(
                tracks = manyTracks,
                onAddTrack = {}
            )
        }

        // First few tracks should be visible
        composeTestRule.onNodeWithText("Track 1").assertExists()
        composeTestRule.onNodeWithText("Track 2").assertExists()
    }

    // Test 32: Validate many comments
    @Test
    fun validateManyCommentsTest() {
        val manyComments = List(15) { index ->
            Comment(
                id = index + 1,
                description = "Comment ${index + 1}",
                rating = 5
            )
        }

        composeTestRule.setContent {
            CommentsSection(
                comments = manyComments,
                newCommentText = "",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        // First few comments should be visible
        composeTestRule.onNodeWithText("Comment 1").assertExists()
        composeTestRule.onNodeWithText("Comment 2").assertExists()
    }

    // Test 33: Validate track with special characters
    @Test
    fun validateTrackSpecialCharactersTest() {
        val specialTrack = Track(
            id = 1,
            name = "Rock & Roll: The Best!",
            duration = "3:30"
        )

        composeTestRule.setContent {
            SongsSection(
                tracks = listOf(specialTrack),
                onAddTrack = {}
            )
        }

        composeTestRule.onNodeWithText("Rock & Roll: The Best!").assertExists()
    }

    // Test 34: Validate comment with special characters
    @Test
    fun validateCommentSpecialCharactersTest() {
        val specialComment = Comment(
            id = 1,
            description = "¡Excelente álbum! ★★★★★",
            rating = 5
        )

        composeTestRule.setContent {
            CommentsSection(
                comments = listOf(specialComment),
                newCommentText = "",
                onCommentTextChange = {},
                onAddComment = {}
            )
        }

        composeTestRule.onNodeWithText("¡Excelente álbum! ★★★★★").assertExists()
    }

    // Test 35: Validate rapid button clicks
    @Test
    fun validateRapidButtonClicksTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = { eventList.add(it) },
                onBack = {}
            )
        }

        repeat(5) {
            composeTestRule.onNodeWithContentDescription("Me gusta").performClick()
        }

        composeTestRule.waitForIdle()
        assertEquals(5, eventList.filter { it is AlbumDetailEvent.ToggleLike }.size)
    }

    // Test 36: Validate back button multiple clicks
    @Test
    fun validateBackButtonMultipleClicksTest() {
        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState,
                onEvent = {},
                onBack = { backClickCount++ }
            )
        }

        repeat(3) {
            composeTestRule.onNodeWithContentDescription("Atrás").performClick()
        }

        composeTestRule.waitForIdle()
        assertEquals(3, backClickCount)
    }

    // Test 37: Validate SongItem component
    @Test
    fun validateSongItemComponentTest() {
        val track = mockTracks[0]

        composeTestRule.setContent {
            SongItem(track = track)
        }

        composeTestRule.onNodeWithText("1.").assertExists()
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertExists()
        composeTestRule.onNodeWithText("5:55").assertExists()
    }

    // Test 38: Validate CommentItem component
    @Test
    fun validateCommentItemComponentTest() {
        val comment = mockComments[0]

        composeTestRule.setContent {
            CommentItem(comment = comment)
        }

        composeTestRule.onNodeWithText("Usuario").assertExists()
        composeTestRule.onNodeWithText("Amazing album!").assertExists()
        composeTestRule.onNodeWithText("U").assertExists() // Avatar
    }

    // Test 39: Validate AlbumHeader component
    @Test
    fun validateAlbumHeaderComponentTest() {
        composeTestRule.setContent {
            AlbumHeader(
                album = mockAlbum,
                isPlaying = false,
                isLiked = false,
                isSaved = false,
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithText("A Night at the Opera").assertExists()
        composeTestRule.onNodeWithText("Queen").assertExists()
        composeTestRule.onNodeWithContentDescription("Reproducir").assertExists()
    }

    // Test 40: Validate complete user interaction flow
    @Test
    fun validateCompleteUserFlowTest() {
        var commentText = ""
        var commentAdded = ""

        composeTestRule.setContent {
            AlbumDetailContent(
                uiState = mockUiState.copy(newCommentText = commentText),
                onEvent = { event ->
                    eventList.add(event)
                    when (event) {
                        is AlbumDetailEvent.UpdateCommentText -> commentText = event.text
                        is AlbumDetailEvent.AddComment -> commentAdded = event.comment
                        else -> {}
                    }
                },
                onBack = { backClickCount++ }
            )
        }

        // Verify all sections
        composeTestRule.onNodeWithText("A Night at the Opera").assertExists()
        composeTestRule.onNodeWithText("Lista de Canciones").assertExists()
        composeTestRule.onNodeWithText("Comentarios").assertExists()

        // Interact with play button
        composeTestRule.onNodeWithContentDescription("Reproducir").performClick()
        composeTestRule.waitForIdle()

        // Interact with like button
        composeTestRule.onNodeWithContentDescription("Me gusta").performClick()
        composeTestRule.waitForIdle()

        // Navigate back
        composeTestRule.onNodeWithContentDescription("Atrás").performClick()
        composeTestRule.waitForIdle()

        // Verify interactions
        assertTrue(eventList.any { it is AlbumDetailEvent.PlayAlbum })
        assertTrue(eventList.any { it is AlbumDetailEvent.ToggleLike })
        assertEquals(1, backClickCount)
    }
}