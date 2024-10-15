// EditMedicamentoDialog.kt

package com.example.recordatorio.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.recordatorio.models.Medicamento

@Composable
fun EditMedicamentoDialog(
    medicamento: Medicamento,
    onDismiss: () -> Unit,
    onSave: (Medicamento) -> Unit
) {
    val nombreState = remember { mutableStateOf(medicamento.nombre) }
    val dosisState = remember { mutableStateOf(medicamento.dosis) }
    val frecuenciaState = remember { mutableStateOf(medicamento.frecuencia) }
    val horarioState = remember { mutableStateOf(medicamento.horario) }
    val unidadState = remember { mutableStateOf(medicamento.unidad) }
    val viaAdministracionState = remember { mutableStateOf(medicamento.viaAdministracion) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text("Editar Medicamento", style = MaterialTheme.typography.headlineMedium)

                TextField(
                    value = nombreState.value,
                    onValueChange = { nombreState.value = it },
                    label = { Text("Nombre") }
                )
                TextField(
                    value = dosisState.value,
                    onValueChange = { dosisState.value = it },
                    label = { Text("Dosis") }
                )
                TextField(
                    value = frecuenciaState.value,
                    onValueChange = { frecuenciaState.value = it },
                    label = { Text("Frecuencia") }
                )
                TextField(
                    value = horarioState.value,
                    onValueChange = { horarioState.value = it },
                    label = { Text("Horario") }
                )
                TextField(
                    value = unidadState.value,
                    onValueChange = { unidadState.value = it },
                    label = { Text("Unidad") }
                )
                TextField(
                    value = viaAdministracionState.value,
                    onValueChange = { viaAdministracionState.value = it },
                    label = { Text("Vía de Administración") }
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    TextButton(onClick = {
                        onSave(medicamento.copy(
                            nombre = nombreState.value,
                            dosis = dosisState.value,
                            frecuencia = frecuenciaState.value,
                            horario = horarioState.value,
                            unidad = unidadState.value,
                            viaAdministracion = viaAdministracionState.value
                        ))
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
