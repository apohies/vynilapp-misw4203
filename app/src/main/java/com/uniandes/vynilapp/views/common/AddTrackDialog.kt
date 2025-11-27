package com.uniandes.vynilapp.views.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.views.SongItem

@Composable
fun SongsSection(
    tracks: List<Track>,
    onAddTrack: (Track) -> Unit
) {
    var showAddForm by remember { mutableStateOf(false) }
    var trackName by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var trackNameError by remember { mutableStateOf<String?>(null) }
    var durationError by remember { mutableStateOf<String?>(null) }

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

        // Formulario inline
        if (showAddForm) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Nueva Canción",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Campo nombre
                    OutlinedTextField(
                        value = trackName,
                        onValueChange = {
                            if (it.length <= 100) {
                                trackName = it
                                trackNameError = null
                            } else {
                                trackNameError = "Máximo 100 caracteres"
                            }
                        },
                        label = { Text("Nombre de la canción") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color.Gray
                        ),
                        isError = trackNameError != null,
                        supportingText = {
                            trackNameError?.let {
                                Text(text = it, color = Color(0xFFF2B8B5))
                            }
                        }
                    )

                    // Campo duración
                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            if (it.length <= 10) {
                                duration = it
                                durationError = if (it.isNotBlank() && !isValidDuration(it)) {
                                    "Formato: MM:SS (ej: 3:45)"
                                } else {
                                    null
                                }
                            }
                        },
                        label = { Text("Duración (MM:SS)") },
                        placeholder = { Text("3:45", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.LightGray,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color.Gray
                        ),
                        isError = durationError != null,
                        supportingText = {
                            durationError?.let {
                                Text(text = it, color = Color(0xFFF2B8B5))
                            }
                        }
                    )

                    // Botones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                showAddForm = false
                                trackName = ""
                                duration = ""
                                trackNameError = null
                                durationError = null
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Gray
                            )
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                if (trackName.isBlank()) {
                                    trackNameError = "El nombre es obligatorio"
                                    return@Button
                                }
                                if (duration.isBlank()) {
                                    durationError = "La duración es obligatoria"
                                    return@Button
                                }
                                if (!isValidDuration(duration)) {
                                    durationError = "Formato inválido"
                                    return@Button
                                }

                                val newTrack = Track(
                                    id = 0,
                                    name = trackName.trim(),
                                    duration = duration.trim()
                                )
                                onAddTrack(newTrack)

                                // Limpiar formulario
                                trackName = ""
                                duration = ""
                                trackNameError = null
                                durationError = null
                                showAddForm = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9C27B0)
                            ),
                            enabled = trackName.isNotBlank() &&
                                    duration.isNotBlank() &&
                                    trackNameError == null &&
                                    durationError == null
                        ) {
                            Text("Guardar", color = Color.White)
                        }
                    }
                }
            }
        }

        // Botón para mostrar formulario
        if (!showAddForm) {
            OutlinedButton(
                onClick = { showAddForm = true },
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
}

// Función de validación (agregar fuera del @Composable)
private fun isValidDuration(duration: String): Boolean {
    val regex = Regex("^\\d{1,2}:\\d{1,2}$")
    if (!regex.matches(duration)) return false

    val parts = duration.split(":")
    val minutes = parts[0].toIntOrNull() ?: return false
    val seconds = parts[1].toIntOrNull() ?: return false

    return seconds < 60 && minutes >= 0
}