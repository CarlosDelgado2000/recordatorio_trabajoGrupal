package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recordatorio.models.RecuperarCuentaModel
import com.example.recordatorio.services.RecuperarCuentaService

@Composable
fun RecuperarCuentaScreen(
    service: RecuperarCuentaService,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Atrás")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recuperar Cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotEmpty()) {
                    service.recoverAccount(RecuperarCuentaModel(email)) { success, errorMessage ->
                        message = if (success) {
                            "Se ha enviado un correo de recuperación."
                        } else {
                            errorMessage ?: "Error al enviar el correo."
                        }
                    }
                } else {
                    message = "Por favor, ingresa tu correo electrónico."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar correo de recuperación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(text = message)
        }
    }
}
