package com.uniandes.vynilapp.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Comment
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.ui.theme.VynilappTheme
import com.uniandes.vynilapp.utils.DateUtils
import com.uniandes.vynilapp.utils.NetworkUtils
import com.uniandes.vynilapp.viewModels.albums.AlbumDetailViewModel
import com.uniandes.vynilapp.views.common.ErrorScreen
import com.uniandes.vynilapp.views.common.LoadingScreen
import com.uniandes.vynilapp.views.common.OfflineErrorScreen
import com.uniandes.vynilapp.views.states.AlbumDetailEvent
import com.uniandes.vynilapp.views.states.AlbumDetailUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    AlbumDetailScreen(
                        modifier = Modifier.padding(paddingValues),
                        albumId = getAlbumIdFromIntent()
                    )
                }
            }
        }
    }
    
    private fun getAlbumIdFromIntent(): Int {
        return intent.getIntExtra(EXTRA_ALBUM_ID, 100) // Valor por defecto
    }
    
    companion object {
        const val EXTRA_ALBUM_ID = "album_id"
        
        fun createIntent(context: android.content.Context, albumId: Int): Intent {
            return Intent(context, AlbumDetailActivity::class.java).apply {
                putExtra(EXTRA_ALBUM_ID, albumId)
            }
        }
    }
}

@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    albumId: Int = 100,
    viewModel: AlbumDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Cargar datos cuando se inicializa la pantalla
    LaunchedEffect(albumId) {
        viewModel.onEvent(AlbumDetailEvent.LoadAlbumById(albumId))
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(modifier = modifier)
        }
        uiState.error != null -> {
            val errorMessage = uiState.error ?: "Error desconocido"
            var isOnline by remember { mutableStateOf(NetworkUtils.isNetworkAvailable(context)) }
            if (!isOnline) {
                OfflineErrorScreen(
                    onRetry = {
                        isOnline = NetworkUtils.isNetworkAvailable(context)
                    }
                )
            } else {
                ErrorScreen(
                    error = errorMessage,
                    onRetry = { viewModel.onEvent(AlbumDetailEvent.LoadAlbumById(albumId)) },
                    modifier = modifier
                )
            }
        }
        else -> {
            AlbumDetailContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = modifier,
                onBack = {
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}

@Composable
fun AlbumDetailContent(
    uiState: AlbumDetailUiState,
    onEvent: (AlbumDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopBar(onBack = onBack)
        }
        
        item {
            AlbumHeader(
                album = uiState.album,
                isPlaying = uiState.isPlaying,
                isLiked = uiState.isLiked,
                isSaved = uiState.isSaved,
                onEvent = onEvent
            )
        }
        
        item {
            SongsSection(
                tracks = uiState.tracks,
                onAddTrack = { track -> onEvent(AlbumDetailEvent.AddTrack(track)) }
            )
        }
        
        item {
            CommentsSection(
                comments = uiState.comments,
                newCommentText = uiState.newCommentText,
                onCommentTextChange = { text -> onEvent(AlbumDetailEvent.UpdateCommentText(text)) },
                onAddComment = { comment -> onEvent(AlbumDetailEvent.AddComment(comment)) }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Atrás",
                tint = Color.White
            )
        }
        
        Text(
            text = "Detalles del Álbum",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        IconButton(onClick = { /* Menú */ }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Menú",
                tint = Color.White
            )
        }
    }
}

@Composable
fun AlbumHeader(
    album: Album?,
    isPlaying: Boolean,
    isLiked: Boolean,
    isSaved: Boolean,
    onEvent: (AlbumDetailEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Portada del álbum
        Box {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray)
                ) {
                    if(album?.cover != null){
                        AsyncImage(
                            model = album?.cover,
                            contentDescription = album?.name,
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "Portada",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }
                }
                // Botones de acción
                Row {
                    IconButton(
                        onClick = { onEvent(AlbumDetailEvent.ToggleLike) }
                    ) {
                        Icon(
                            if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Me gusta",
                            tint = if (isLiked) Color.Red else Color.White
                        )
                    }

                    IconButton(
                        onClick = { onEvent(AlbumDetailEvent.ToggleSave) }
                    ) {
                        Icon(
                            if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (isSaved) Color(0xFF9C27B0) else Color.White
                        )
                    }

                    IconButton(
                        onClick = { onEvent(AlbumDetailEvent.ShareAlbum) }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        
        // Información del álbum
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = album?.name ?: "Cargando...",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = album?.performers?.firstOrNull()?.name ?: "Artista",
                color = Color.Gray,
                fontSize = 16.sp
            )
            
            Text(
                text = "${album?.genre ?: "Género"} • ${DateUtils.formatAlbumYear(album?.releaseDate)}",
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Text(
                text = DateUtils.formatAlbumDate(album?.releaseDate),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        
        // Botón de play
        FloatingActionButton(
            onClick = { 
                if (isPlaying) {
                    onEvent(AlbumDetailEvent.PauseAlbum)
                } else {
                    onEvent(AlbumDetailEvent.PlayAlbum)
                }
            },
            modifier = Modifier.size(56.dp),
            containerColor = Color(0xFF9C27B0)
        ) {
            Icon(
                if (isPlaying) Icons.Default.Refresh else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Detener" else "Reproducir",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SongsSection(
    tracks: List<Track>,
    onAddTrack: (Track) -> Unit
) {
    Column {
        Text(
            text = "Lista de Canciones",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        tracks.forEach { track ->
            SongItem(track = track)
        }
        
        // Botón para agregar canción
        OutlinedButton(
            onClick = { 
                val newTrack = Track(
                    id = tracks.size + 1,
                    name = "Nueva Canción",
                    duration = "0:00"
                )
                onAddTrack(newTrack)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.Gray.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Agregar",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar nueva canción",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SongItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${track.id}.",
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Text(
                text = track.name,
                color = Color.White,
                fontSize = 16.sp
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = track.duration,
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            IconButton(onClick = { /* Menú de canción */ }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Song menu",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CommentsSection(
    comments: List<Comment>,
    newCommentText: String,
    onCommentTextChange: (String) -> Unit,
    onAddComment: (String) -> Unit
) {
    Column {
        Text(
            text = "Comentarios",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        comments.forEach { comment ->
            CommentItem(comment = comment)
        }
        
        // Campo para agregar comentario
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newCommentText,
                onValueChange = onCommentTextChange,
                placeholder = {
                    Text(
                        text = "Añadir un comentario...",
                        color = Color.Gray
                    )
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = { 
                    if (newCommentText.isNotBlank()) {
                        onAddComment(newCommentText)
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFF9C27B0),
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar del usuario
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Text(
                text = "U",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 12.sp
            )
        }
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Usuario",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "Hace 2 días",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFE1BEE7).copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = comment.description,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
