package com.example.recordatorio.services

import com.example.recordatorio.repository.UserRepository

class UserService(private val userRepository: UserRepository) {

    // Método para verificar al usuario (inicio de sesión)
    fun verifyUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        userRepository.login(email, password) { success, errorMessage ->
            onComplete(success, errorMessage)
        }
    }
}
