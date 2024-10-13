package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.recordatorio.R

@Composable
fun PortadaScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen pequeña a la izquierda y botón de iniciar sesión a la derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_1), // Icono pequeño
                contentDescription = null,
                modifier = Modifier.size(80.dp) // Tamaño reducido
            )
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.Top)
            ) {
                Text("Iniciar sesión", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Texto de bienvenida centrado
        Text(
            text = "¡Bienvenido!",
            color = Color.Red,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen principal centrada
        Image(
            painter = painterResource(id = R.drawable.img), // Imagen grande
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto descriptivo
        Text(
            text = "Aquí, te ayudamos a mantener el control de tu bienestar recordándote tomar tus medicamentos a tiempo.\n" +
                    "Porque cada dosis es un paso hacia sentirte mejor. ¡Estamos aquí para cuidarte y acompañarte en tu camino hacia la salud!",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de "Comenzar" centrado
        Button(
            onClick = onRegisterClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp) // Espacio horizontal en los bordes
        ) {
            Text("Registrar", color = Color.White)
        }
    }
}
