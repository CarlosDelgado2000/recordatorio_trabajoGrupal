package com.example.recordatorio

import com.google.firebase.ktx.initialize
import com.google.firebase.ktx.Firebase
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
import com.example.recordatorio.services.MedicamentoService // Importar el servicio de medicamentos
import com.example.recordatorio.ui.theme.RecordatorioTheme
import com.example.recordatorio.ui.theme.screens.*
import com.google.firebase.auth.ktx.auth
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var userService: UserService
    private lateinit var recuperarCuentaService: RecuperarCuentaService
    private lateinit var registroService: RegistroService
    private lateinit var medicamentoService: MedicamentoService // Agregar el servicio de medicamentos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        Firebase.initialize(this)

        // Crear las instancias de UserRepository y los servicios
        val userRepository = UserRepository()
        userService = UserService(userRepository)
        registroService = RegistroService(userRepository)
        recuperarCuentaService = RecuperarCuentaService(userRepository)
        medicamentoService = MedicamentoService() // Inicializar el servicio de medicamentos

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
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
                        onLogoutClick = {
                            Firebase.auth.signOut()
                            currentScreen = Screen.Portada
                        },
                        onAddMedicamentoClick = { currentScreen = Screen.AgregarMedicamento },
                        onSettingsClick = { currentScreen = Screen.Configuracion }
                    )
                    is Screen.VerAgenda -> VerAgendaScreen(
                        medicamentoService = medicamentoService, // Pasar el servicio
                        onBackClick = { currentScreen = Screen.Medicamento }
                    )
                    is Screen.AgregarMedicamento -> AgregarMedicamentoScreen(
                        medicamentoService = medicamentoService, // Pasar el servicio
                        onBackClick = { currentScreen = Screen.Medicamento } // Llamar al callback para volver a la pantalla de medicamentos
                    )
                    is Screen.Configuracion -> ConfiguracionScreen(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        onBackClick = { currentScreen = Screen.Medicamento },
                        onLogoutClick = {
                            Firebase.auth.signOut()
                            currentScreen = Screen.Portada
                        }
                    )
                    else -> {}
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
