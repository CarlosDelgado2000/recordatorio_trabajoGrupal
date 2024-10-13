package com.example.recordatorio.repository

import com.google.firebase.auth.FirebaseAuth

class UserRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Método para registrar un nuevo usuario en Firebase
    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Registro exitoso
                } else {
                    onComplete(false, task.exception?.message) // Fallo en el registro
                }
            }
    }

    // Método de inicio de sesión con Firebase
    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Inicio de sesión exitoso
                } else {
                    onComplete(false, task.exception?.message) // Error de autenticación
                }
            }
    }

    // Método para enviar un correo de recuperación de contraseña
    fun recoverAccount(email: String, onComplete: (Boolean, String?) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Correo de recuperación enviado
                } else {
                    onComplete(false, task.exception?.message) // Error al enviar el correo
                }
            }
    }
}
