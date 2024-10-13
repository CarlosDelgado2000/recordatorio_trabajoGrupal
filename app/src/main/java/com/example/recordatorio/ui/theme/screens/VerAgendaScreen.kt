package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recordatorio.models.Medicamento

@Composable
fun VerAgendaScreen(
    medicamentos: List<Medicamento>,
    onBackClick: () -> Unit
) {
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
            text = "Agenda de Medicamentos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(medicamentos) { medicamento ->
                MedicamentoItem(medicamento = medicamento)
            }
        }
    }
}

@Composable
fun MedicamentoItem(medicamento: Medicamento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${medicamento.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Dosis: ${medicamento.dosis}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Horario: ${medicamento.horario}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
