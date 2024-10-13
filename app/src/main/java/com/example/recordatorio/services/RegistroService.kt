package com.example.recordatorio.services

import com.example.recordatorio.repository.UserRepository

class RegistroService(private val userRepository: UserRepository) {
    fun registerAccount(email: String, password: String, confirmPassword: String, callback: (Boolean, String?) -> Unit) {
        if (password == confirmPassword) {
            // Usa el UserRepository para registrar el usuario en Firebase
            userRepository.register(email, password) { success, errorMessage ->
                callback(success, errorMessage) // Llama al callback con el resultado
            }
        } else {
            callback(false, "Las contraseñas no coinciden.")
        }
    }
}
