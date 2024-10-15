package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.example.recordatorio.models.Medicamento
import com.example.recordatorio.services.MedicamentoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AgregarMedicamentoScreen(
    medicamentoService: MedicamentoService, // Inyectar el servicio
    onBackClick: () -> Unit // Callback para regresar a la pantalla anterior
) {
    // Estado para los campos de entrada
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var dosis by remember { mutableStateOf(TextFieldValue("")) }
    var frecuencia by remember { mutableStateOf(TextFieldValue("")) }
    var unidad by remember { mutableStateOf(TextFieldValue("")) }
    var horario by remember { mutableStateOf(TextFieldValue("")) }
    var viaAdministracion by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isSuccessDialogVisible by remember { mutableStateOf(false) } // Estado para el diálogo de éxito

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Agregar Medicamento", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dosis,
            onValueChange = { dosis = it },
            label = { Text("Dosis") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = frecuencia,
            onValueChange = { frecuencia = it },
            label = { Text("Frecuencia") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = unidad,
            onValueChange = { unidad = it },
            label = { Text("Unidad") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = horario,
            onValueChange = { horario = it },
            label = { Text("Horario") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viaAdministracion,
            onValueChange = { viaAdministracion = it },
            label = { Text("Vía de Administración") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el indicador de carga si está cargando
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        } else {
            Button(onClick = {
                // Limpiar mensaje de error
                errorMessage = ""

                // Validar campos antes de continuar
                if (nombre.text.isBlank() || dosis.text.isBlank() || frecuencia.text.isBlank() ||
                    unidad.text.isBlank() || horario.text.isBlank() || viaAdministracion.text.isBlank()) {
                    errorMessage = "Por favor, completa todos los campos."
                    return@Button
                }

                // Crear un nuevo medicamento
                val nuevoMedicamento = Medicamento(
                    nombre = nombre.text,
                    dosis = dosis.text,
                    frecuencia = frecuencia.text,
                    unidad = unidad.text,
                    horario = horario.text,
                    viaAdministracion = viaAdministracion.text
                )

                // Agregar medicamento a Firebase
                isLoading = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        medicamentoService.addMedicamento(nuevoMedicamento)
                        // Limpiar los campos después de registrar
                        withContext(Dispatchers.Main) {
                            nombre = TextFieldValue("")
                            dosis = TextFieldValue("")
                            frecuencia = TextFieldValue("")
                            unidad = TextFieldValue("")
                            horario = TextFieldValue("")
                            viaAdministracion = TextFieldValue("")
                            isSuccessDialogVisible = true // Mostrar el diálogo de éxito
                        }
                    } catch (e: Exception) {
                        // Manejar error
                        withContext(Dispatchers.Main) {
                            errorMessage = "Error al agregar medicamento: ${e.message}"
                        }
                    } finally {
                        isLoading = false
                    }
                }
            }) {
                Text("Registrar Medicamento")
            }
        }

        // Mensaje de error si existe
        if (errorMessage.isNotBlank()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBackClick) {
            Text("Volver")
        }
    }

    // Diálogo de éxito
    if (isSuccessDialogVisible) {
        SuccessDialog(onDismiss = { isSuccessDialogVisible = false })
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Registro Exitoso", color = androidx.compose.ui.graphics.Color.Green)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Aquí puedes poner un logo o imagen
                Text(text = "✔️", fontSize = 48.sp, modifier = Modifier.padding(bottom = 8.dp)) // Ícono de visto
                Text(text = "Medicamento agregado correctamente.", style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}

@Composable
fun SuccessDialogBackground(onDismiss: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color.White // Cambia este color según tus necesidades
    ) {
        SuccessDialog(onDismiss = onDismiss)
    }
}
