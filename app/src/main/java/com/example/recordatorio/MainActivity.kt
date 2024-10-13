package com.example.recordatorio

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.recordatorio.models.Medicamento
import com.example.recordatorio.repository.UserRepository
import com.example.recordatorio.services.RecuperarCuentaService
import com.example.recordatorio.services.RegistroService
import com.example.recordatorio.services.UserService
import com.example.recordatorio.ui.theme.RecordatorioTheme
import com.example.recordatorio.ui.theme.screens.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.initialize

class MainActivity : ComponentActivity() {
    private lateinit var userService: UserService
    private lateinit var recuperarCuentaService: RecuperarCuentaService
    private lateinit var registroService: RegistroService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        Firebase.initialize(this)

        // Crear las instancias de UserRepository y los servicios
        val userRepository = UserRepository()
        userService = UserService(userRepository)
        registroService = RegistroService(userRepository)
        recuperarCuentaService = RecuperarCuentaService(userRepository)

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) } // Estado para el tema oscuro
            RecordatorioTheme(isDarkTheme = isDarkTheme, onThemeChange = { isDarkTheme = it }) {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Portada) }
                val medicamentos = remember { mutableStateListOf<Medicamento>() }

                // Control del flujo entre pantallas
                when (currentScreen) {
                    is Screen.Portada -> PortadaScreen(
                        onLoginClick = { currentScreen = Screen.Login },
                        onRegisterClick = { currentScreen = Screen.Registro }
                    )
                    is Screen.Login -> LoginScreen(
                        userService = userService,
                        onLoginClick = { email, password ->
                            userService.verifyUser(email, password) { success, errorMessage ->
                                if (success) {
                                    currentScreen = Screen.Medicamento
                                } else {
                                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        onBackClick = { currentScreen = Screen.Portada },
                        onRecoverClick = { currentScreen = Screen.RecuperarCuenta }
                    )
                    is Screen.RecuperarCuenta -> RecuperarCuentaScreen(
                        service = recuperarCuentaService,
                        onBackClick = { currentScreen = Screen.Login }
                    )
                    is Screen.Registro -> RegistroScreen(
                        service = registroService,
                        onBackClick = { currentScreen = Screen.Portada },
                        onRegisterSuccess = { email, password ->
                            Toast.makeText(this, "Registro exitoso. Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show()
                            currentScreen = Screen.Login
                        }
                    )
                    is Screen.Medicamento -> MedicamentoScreen(
                        medicamentos = medicamentos,
                        onViewAgendaClick = { currentScreen = Screen.VerAgenda },
                        onLogoutClick = { // Aquí se maneja el cierre de sesión
                            Firebase.auth.signOut()
                            currentScreen = Screen.Portada // Cerrar sesión
                        },
                        onAddMedicamentoClick = { currentScreen = Screen.AgregarMedicamento },
                        onSettingsClick = { currentScreen = Screen.Configuracion }
                    )
                    is Screen.VerAgenda -> VerAgendaScreen(
                        medicamentos = medicamentos,
                        onBackClick = { currentScreen = Screen.Medicamento }
                    )
                    is Screen.AgregarMedicamento -> AgregarMedicamentoScreen(
                        onRegisterClick = { nombreMedicamento, dosis, horario ->
                            medicamentos.add(Medicamento(nombreMedicamento, dosis, horario, "08:00 AM"))
                            Toast.makeText(this, "Medicamento '$nombreMedicamento' registrado.", Toast.LENGTH_LONG).show()
                            currentScreen = Screen.Medicamento
                        },
                        onBackClick = { currentScreen = Screen.Medicamento }
                    )
                    is Screen.Configuracion -> ConfiguracionScreen(
                        isDarkTheme = isDarkTheme, // Pasamos el estado del tema oscuro
                        onThemeChange = { isDarkTheme = it }, // Función para cambiar el tema
                        onBackClick = { currentScreen = Screen.Medicamento },
                        onLogoutClick = { // Manejo del cierre de sesión
                            Firebase.auth.signOut()
                            currentScreen = Screen.Portada // Cerrar sesión
                        }
                    )
                    else -> {} // Manejo del caso no esperado
                }
            }
        }
    }
}

// Enum para las pantallas
sealed class Screen {
    object Portada : Screen()
    object Login : Screen()
    object RecuperarCuenta : Screen()
    object Registro : Screen()
    object Medicamento : Screen()
    object VerAgenda : Screen()
    object AgregarMedicamento : Screen()
    object Configuracion : Screen() // Nueva pantalla para configuraciones
}
