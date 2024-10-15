// models/Medicamento.kt
package com.example.recordatorio.models

data class Medicamento(
    val id: String = "",
    val nombre: String = "",
    val dosis: String = "",
    val frecuencia: String = "",
    val unidad: String = "",
    val viaAdministracion: String = "",
    val horario: String = ""
)
