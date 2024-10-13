package com.example.recordatorio.repository

import com.example.recordatorio.models.Medicamento

class MedicamentoRepository {
    // Lista simulada de medicamentos
    private val medicamentos = mutableListOf(
        Medicamento(1.toString(), "Paracetamol", "500mg", "08:00 AM"),
        Medicamento(2.toString(), "Ibuprofeno", "200mg", "01:00 PM"),
        Medicamento(3.toString(), "Amoxicilina", "250mg", "06:00 PM"),
        Medicamento(4.toString(), "Cetirizina", "10mg", "09:00 PM"),
        Medicamento(5.toString(), "Losartán", "50mg", "10:00 AM")
        // Agrega más medicamentos según sea necesario
    )

    fun getMedicamentos(): List<Medicamento> {
        return medicamentos
    }

    fun searchMedicamentos(query: String): List<Medicamento> {
        return medicamentos.filter { medicamento ->
            medicamento.nombre.contains(query, ignoreCase = true)
        }
    }
}
