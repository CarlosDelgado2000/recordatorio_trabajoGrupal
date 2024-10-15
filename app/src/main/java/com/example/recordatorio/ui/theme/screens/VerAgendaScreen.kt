package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recordatorio.models.Medicamento
import com.example.recordatorio.services.MedicamentoService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerAgendaScreen(
    medicamentoService: MedicamentoService,
    onBackClick: () -> Unit
) {
    var medicamentos by remember { mutableStateOf(listOf<Medicamento>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var editableMedicamento by remember { mutableStateOf<Medicamento?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var medicamentoToDelete by remember { mutableStateOf<Medicamento?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Cargar medicamentos desde Firestore (solo una vez al inicio)
    LaunchedEffect(Unit) {
        loadMedicamentos(medicamentoService, onError = { errorMessage = it }) { loadedMedicamentos ->
            medicamentos = loadedMedicamentos
            isLoading = false
        }
    }

    // UI para mostrar medicamentos
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Ver Agenda") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        if (isLoading) {
            LoadingScreen() // Mostrar pantalla de carga
        } else {
            if (errorMessage.isNotEmpty()) {
                ErrorScreen(errorMessage) { onBackClick() } // Pantalla de error
            } else {
                LazyColumn {
                    items(medicamentos) { medicamento ->
                        MedicamentoItem(
                            medicamento = medicamento,
                            onEditClick = {
                                editableMedicamento = medicamento
                                showEditDialog = true
                            },
                            onDeleteClick = {
                                medicamentoToDelete = medicamento
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }

                // Diálogo de edición
                if (showEditDialog) {
                    editableMedicamento?.let { medicamento ->
                        EditMedicamentoDialog(
                            medicamento = medicamento,
                            onDismiss = { showEditDialog = false },
                            onSave = { updatedMedicamento ->
                                coroutineScope.launch {
                                    // Actualizar el medicamento directamente en la lista
                                    medicamentos = medicamentos.map {
                                        if (it.id == updatedMedicamento.id) updatedMedicamento else it
                                    }
                                    showEditDialog = false
                                }
                            }
                        )
                    }
                }

                // Diálogo de confirmación de eliminación
                if (showDeleteConfirmDialog && medicamentoToDelete != null) {
                    ConfirmDeleteDialog(
                        medicamento = medicamentoToDelete!!,
                        onDismiss = { showDeleteConfirmDialog = false },
                        onConfirm = {
                            coroutineScope.launch {
                                deleteMedicamento(
                                    medicamentoToDelete!!.id,
                                    medicamentoService,
                                    onError = { errorMessage = it },
                                    onSuccess = {
                                        medicamentos = medicamentos.filterNot { it.id == medicamentoToDelete!!.id }
                                        showDeleteConfirmDialog = false
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

// Función para cargar medicamentos desde el servicio
private suspend fun loadMedicamentos(
    medicamentoService: MedicamentoService,
    onError: (String) -> Unit,
    onLoadingComplete: (List<Medicamento>) -> Unit
) {
    try {
        val medicamentos = medicamentoService.getMedicamentos()
        onLoadingComplete(medicamentos)
    } catch (e: Exception) {
        onError("Error al cargar medicamentos: ${e.message}")
        onLoadingComplete(emptyList())
    }
}

// Función para eliminar un medicamento
suspend fun deleteMedicamento(
    id: String,
    medicamentoService: MedicamentoService,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    try {
        medicamentoService.deleteMedicamento(id) // Asumiendo que esta es la llamada a tu API
        onSuccess()
    } catch (e: Exception) {
        onError(e.message ?: "Error al eliminar el medicamento")
    }
}

// Pantallas de carga y error
@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando medicamentos...")
    }
}

@Composable
fun ErrorScreen(errorMessage: String, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(errorMessage, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text("Volver")
        }
    }
}

// Composable para mostrar cada item de medicamento
@Composable
fun MedicamentoItem(
    medicamento: Medicamento,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${medicamento.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Dosis: ${medicamento.dosis}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Frecuencia: ${medicamento.frecuencia}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Horario: ${medicamento.horario}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Unidad: ${medicamento.unidad}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Vía de Administración: ${medicamento.viaAdministracion}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

// Diálogo de confirmación para la eliminación de un medicamento
@Composable
fun ConfirmDeleteDialog(
    medicamento: Medicamento,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar eliminación") },
        text = { Text("¿Estás seguro de que deseas eliminar el medicamento ${medicamento.nombre}?") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
