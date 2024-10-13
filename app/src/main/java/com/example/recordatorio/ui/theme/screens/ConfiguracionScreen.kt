package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(
    isDarkTheme: Boolean, // Recibe el estado del tema oscuro
    onThemeChange: (Boolean) -> Unit, // Recibe la función para cambiar el tema
    onBackClick: () -> Unit, // Callback para volver a la pantalla anterior
    onLogoutClick: () -> Unit // Callback para cerrar sesión
) {
    // Estado para el tamaño de la fuente
    var fontSize by remember { mutableStateOf(16f) } // Tamaño de fuente inicial

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Opción para activar/desactivar el tema oscuro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Modo oscuro")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange(it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Control deslizante para ajustar el tamaño de la letra
            Text(text = "Tamaño de letra: ${fontSize.toInt()}", fontSize = fontSize.sp)
            Slider(
                value = fontSize,
                onValueChange = { fontSize = it },
                valueRange = 12f..30f, // Rango de tamaños de letra
                steps = 18 // Cantidad de pasos entre 12 y 30
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para cerrar sesión
            Button(
                onClick = {
                    // Cierra sesión usando Firebase
                    Firebase.auth.signOut()
                    onLogoutClick() // Invoca el callback para manejar el cierre de sesión en la UI
                }
            ) {
                Text("Cerrar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de regreso
            Button(onClick = { onBackClick() }) {
                Text("Volver")
            }
        }
    }
}
