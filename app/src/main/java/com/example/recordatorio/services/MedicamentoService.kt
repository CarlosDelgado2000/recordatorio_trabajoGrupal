package com.example.recordatorio.services

import com.example.recordatorio.models.Medicamento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MedicamentoService {

    private val firestore = FirebaseFirestore.getInstance()

    // Agregar un medicamento a Firebase
    suspend fun addMedicamento(medicamento: Medicamento): String {
        try {
            val documentRef = firestore.collection("medicamentos")
                .add(medicamento)
                .await()
            return documentRef.id  // Retornar el ID generado por Firestore
        } catch (e: Exception) {
            throw Exception("Error al agregar medicamento: ${e.message}")
        }
    }


    // Obtener todos los medicamentos de Firebase
    suspend fun getMedicamentos(): List<Medicamento> {
        return try {
            firestore.collection("medicamentos")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Medicamento::class.java) }
        } catch (e: Exception) {
            // Manejo de errores al obtener medicamentos
            throw Exception("Error al obtener medicamentos: ${e.message}")
        }
    }

    // Buscar medicamentos por nombre
    suspend fun searchMedicamentos(query: String): List<Medicamento> {
        return getMedicamentos().filter { medicamento ->
            medicamento.nombre.contains(query, ignoreCase = true)
        }
    }

    // Obtener medicamentos en tiempo real
    fun getMedicamentosRealTime(onUpdate: (List<Medicamento>) -> Unit) {
        firestore.collection("medicamentos")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Manejo de errores al escuchar cambios
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val medicamentos = snapshot.documents.mapNotNull { it.toObject(Medicamento::class.java) }
                    onUpdate(medicamentos)
                }
            }
    }

    // Actualizar un medicamento en Firebase
    suspend fun updateMedicamento(medicamento: Medicamento) {
        try {
            firestore.collection("medicamentos")
                .document(medicamento.id)  // Asumiendo que el id está en el modelo
                .set(medicamento)  // Actualiza el documento existente
                .await()
        } catch (e: Exception) {
            // Manejo de errores al actualizar medicamento
            throw Exception("Error al actualizar medicamento: ${e.message}")
        }
    }

    // Eliminar un medicamento por ID
    suspend fun deleteMedicamento(medicamentoId: String) {
        try {
            firestore.collection("medicamentos")
                .document(medicamentoId)
                .delete()  // Elimina el documento
                .await()
        } catch (e: Exception) {
            // Manejo de errores al eliminar medicamento
            throw Exception("Error al eliminar medicamento: ${e.message}")
        }
    }
}
