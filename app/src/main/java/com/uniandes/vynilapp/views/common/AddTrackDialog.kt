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

@Composable
fun AddTrackDialog(
    onDismiss: () -> Unit,
    onConfirm: (trackName: String, duration: String) -> Unit
) {
    var trackName by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var trackNameError by remember { mutableStateOf<String?>(null) }
    var durationError by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Agregar Nueva Canción",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Campo nombre de la canción
                OutlinedTextField(
                    value = trackName,
                    onValueChange = {
                        if (it.length <= 100) {
                            trackName = it
                            trackNameError = null
                        } else {
                            trackNameError = "El nombre debe tener menos de 100 caracteres"
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
                            Text(
                                text = it,
                                color = Color(0xFFF2B8B5)
                            )
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
                            Text(
                                text = it,
                                color = Color(0xFFF2B8B5)
                            )
                        }
                    }
                )

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Gray
                        )
                    ) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

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
                                durationError = "Formato inválido (use MM:SS)"
                                return@Button
                            }

                            onConfirm(trackName.trim(), duration.trim())
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9C27B0)
                        ),
                        enabled = trackName.isNotBlank() &&
                                duration.isNotBlank() &&
                                trackNameError == null &&
                                durationError == null
                    ) {
                        Text("Agregar", color = Color.White)
                    }
                }
            }
        }
    }
}

// Función para validar formato de duración
private fun isValidDuration(duration: String): Boolean {
    val regex = Regex("^\\d{1,2}:\\d{1,2}$")
    if (!regex.matches(duration)) return false

    val parts = duration.split(":")
    val minutes = parts[0].toIntOrNull() ?: return false
    val seconds = parts[1].toIntOrNull() ?: return false

    return seconds < 60 && minutes >= 0
}