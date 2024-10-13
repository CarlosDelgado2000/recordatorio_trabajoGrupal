package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AgregarMedicamentoScreen(onRegisterClick: (String, String, String) -> Unit, onBackClick: () -> Unit) {
    var nombreMedicamento by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") } // Campo para el horario

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombreMedicamento,
            onValueChange = { nombreMedicamento = it },
            label = { Text("Nombre del Medicamento") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = dosis,
            onValueChange = { dosis = it },
            label = { Text("Dosis") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = horario,
            onValueChange = { horario = it }, // Cambiar el valor de horario aquí
            label = { Text("Horario") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (nombreMedicamento.isNotEmpty() && dosis.isNotEmpty() && horario.isNotEmpty()) {
                onRegisterClick(nombreMedicamento, dosis, horario) // Asegúrate de pasar los tres parámetros
            }
        }) {
            Text("Registrar Medicamento")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClick) {
            Text("Volver")
        }
    }
}
