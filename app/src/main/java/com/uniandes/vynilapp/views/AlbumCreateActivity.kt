package com.uniandes.vynilapp.views

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.ui.theme.VynilappTheme
import com.uniandes.vynilapp.viewModels.albums.AlbumCreateUiState
import com.uniandes.vynilapp.viewModels.albums.AlbumCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AlbumCreateActivity(): ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    AlbumCreateScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

private const val MAX_TITLE_LENGTH = 50
private const val MAX_DESCRIPTION_LENGTH = 300
private const val MAX_GENRE_LENGTH = 20
private const val MAX_RECORD_LABEL_LENGTH = 30
private const val RELEASE_DATE_LENGTH = 10
private const val MAX_IMG_URL_LENGTH = 200
@Composable
fun AlbumCreateScreen(modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String?>(null) }
    var releaseDate by remember { mutableStateOf("") }
    var releaseDateError by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var genre by remember { mutableStateOf("") }
    var genreError by remember { mutableStateOf<String?>(null) }
    var recordLabel by remember { mutableStateOf("") }
    var recordLabelError by remember { mutableStateOf<String?>(null) }
    var imageUrl by remember { mutableStateOf("") }
    var imageUrlError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val viewModel: AlbumCreateViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is AlbumCreateUiState.Success) {
            // close the activity when creation succeeded
            (context as? Activity)?.finish()
        }
    }

    val showErrorDialog = uiState is AlbumCreateUiState.Error
    val errorMessage = (uiState as? AlbumCreateUiState.Error)?.message ?: "Unknown error"

    val isReleaseDateValid = remember(releaseDate) { parseReleaseDate(releaseDate) != null }
    val isFormValid = remember(title, releaseDate, description, genre, recordLabel, imageUrl) {
        title.isNotBlank()
                && releaseDate.isNotBlank()
                && isReleaseDateValid
                && description.isNotBlank()
                && genre.isNotBlank()
                && recordLabel.isNotBlank()
                && imageUrl.isNotBlank()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Create Album",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Title
        item {
            OutlinedTextField(
                value = title,
                onValueChange = { newTitle ->
                    if (newTitle.length <= MAX_TITLE_LENGTH) {
                        title = newTitle
                        titleError = null
                    } else {
                        titleError = "El título debe tener menos de $MAX_TITLE_LENGTH caracteres"
                    } },
                label = { Text("Album Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = titleError != null
            )
        }

        // Release Date
        item {
            OutlinedTextField(
                value = releaseDate,
                onValueChange = { newReleaseDate ->
                    if (newReleaseDate.length <= RELEASE_DATE_LENGTH) {
                        releaseDate = newReleaseDate
                        releaseDateError = if(newReleaseDate.isNotBlank() && parseReleaseDate(newReleaseDate) == null) {
                            "The date must be in format YYYY-MM-DD"
                        } else {
                            ""
                        }
                    } else {
                        releaseDateError = "The date must have less than $RELEASE_DATE_LENGTH characters"
                    } },
                label = { Text("Release Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = releaseDateError != null && releaseDateError != ""
            )
            if (releaseDateError != null) {
                Text(
                    text = releaseDateError!!,
                    color = Color(242, 184, 181),
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        // Description
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { newDescription ->
                    if (newDescription.length <= MAX_DESCRIPTION_LENGTH) {
                        description = newDescription
                        descriptionError = null
                    } else {
                        descriptionError = "La descripción debe tener menos de $MAX_DESCRIPTION_LENGTH caracteres"
                    } },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = descriptionError != null
            )
        }

        // Genre
        item {
            OutlinedTextField(
                value = genre,
                onValueChange = { newGenre ->
                    if (newGenre.length <= MAX_GENRE_LENGTH) {
                        genre = newGenre
                        genreError = null
                    } else {
                        genreError = "El género debe tener menos de $MAX_GENRE_LENGTH caracteres"
                    } },
                label = { Text("Genre") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = genreError != null
            )
        }

        // Record Label
        item {
            OutlinedTextField(
                value = recordLabel,
                onValueChange = { newLabel ->
                    if (newLabel.length <= MAX_RECORD_LABEL_LENGTH) {
                        recordLabel = newLabel
                        recordLabelError = null
                    } else {
                        recordLabelError = "La disquera debe tener menos de $MAX_RECORD_LABEL_LENGTH caracteres"
                    }  },
                label = { Text("Record Label") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = recordLabelError != null
            )
        }

        // Record Label
        item {
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { newImage ->
                    if (newImage.length <= MAX_IMG_URL_LENGTH) {
                        imageUrl = newImage
                        imageUrlError = null
                    } else {
                        imageUrlError = "El género debe tener menos de $MAX_IMG_URL_LENGTH caracteres"
                    }  },
                label = { Text("Albúm cover url") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = imageUrlError != null
            )
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    val album = Album(
                        id = 0,
                        name = title,
                        cover = imageUrl,
                        releaseDate = releaseDate,
                        description = description,
                        genre = genre,
                        recordLabel = recordLabel,
                        tracks = emptyList(),
                        performers = emptyList()
                    )
                    viewModel.createAlbum(album)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                ),
                enabled = isFormValid
            ) {
                Text("Create Album", color = Color.White)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.resetState() },
            confirmButton = {
                TextButton(onClick = { viewModel.resetState() }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text(errorMessage) }
        )
    }
}

fun parseReleaseDate(input: String?): Date? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }
    if (input.isNullOrBlank()) return null
    return try {
        inputFormat.parse(input)
    } catch (e: ParseException) {
        null
    }
}