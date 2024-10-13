package com.example.recordatorio.services

import com.example.recordatorio.models.RecuperarCuentaModel
import com.example.recordatorio.repository.UserRepository

class RecuperarCuentaService(private val repository: UserRepository) {
    fun recoverAccount(
        cuenta: RecuperarCuentaModel,
        callback: (Boolean, String?) -> Unit
    ) {
        repository.recoverAccount(cuenta.email) { success, errorMessage ->
            // Aquí puedes manejar el resultado
            callback(success, errorMessage) // Llama al callback con el resultado de recoverAccount
        }
    }
}

