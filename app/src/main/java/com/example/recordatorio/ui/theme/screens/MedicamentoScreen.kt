package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.painterResource
import com.example.recordatorio.R
import com.example.recordatorio.models.Medicamento
import com.example.recordatorio.repository.MedicamentoRepository
import com.example.recordatorio.services.MedicamentoService

@Composable
fun MedicamentoScreen(
    medicamentos: List<Medicamento>,
    onViewAgendaClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAddMedicamentoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val repository = MedicamentoRepository()
    val service = MedicamentoService(repository)

    var searchQuery by remember { mutableStateOf("") }
    var filteredMedicamentos by remember { mutableStateOf(medicamentos) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                filteredMedicamentos = service.searchMedicamentos(searchQuery)
            },
            label = { Text("Buscar medicamento") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            trailingIcon = {
                IconButton(onClick = { buscarMedicamento(searchQuery) }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar",
                        tint = Color.Gray
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recuerda, cada pastilla es un paso hacia una vida más saludable.\n¡No olvides tu medicamento hoy!",
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Para agregar un nuevo medicamento, presiona el botón 'Agregar Medicamento' y completa los detalles requeridos.",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onViewAgendaClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agenda", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddMedicamentoClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Medicamento", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSettingsClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Más", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredMedicamentos) { medicamento ->
                Text(
                    text = medicamento.nombre,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.img_2),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}

private fun buscarMedicamento(query: String) {
    if (query.isNotBlank()) {
        println("Buscando medicamento: $query")
    } else {
        println("Por favor, ingrese un medicamento para buscar.")
    }
}
