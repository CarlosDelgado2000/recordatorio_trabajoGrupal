package com.example.recordatorio.services

import com.example.recordatorio.models.Medicamento
import com.example.recordatorio.repository.MedicamentoRepository

class MedicamentoService(private val repository: MedicamentoRepository) {
    fun searchMedicamentos(query: String): List<Medicamento> {
        return repository.getMedicamentos().filter { medicamento ->
            medicamento.nombre.contains(query, ignoreCase = true)
        }
    }
}
