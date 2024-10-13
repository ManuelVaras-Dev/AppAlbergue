package com.example.appalbergue

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar el fragmento inicial (Cargando)
        if (savedInstanceState == null) {
            loadFragment(CargandoFragment()) // Carga el fragmento de carga inicial
        }
    }

    // Método para cargar fragmentos
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main, fragment) // Asegúrate de que el ID coincida con tu contenedor
        transaction.addToBackStack(null) // Agregar a la pila de retroceso
        transaction.commit()
    }

    // Método para regresar al fragmento anterior
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack() // Regresar al fragmento anterior
        } else {
            super.onBackPressed() // Comportamiento predeterminado
        }
    }

    // Método para mostrar el Fragmento de Login
    fun showLoginFragment() {
        loadFragment(LoginFragment()) // Asegúrate de que LoginFragment esté definido
    }

    // Método para cargar el Fragmento de Verificación
    fun showVerificarFragment() {
        loadFragment(VerificacionUsuarioragment())
    }
    fun irARegistroAdministrador() {
        // Este método cargará el fragmento SeleccionDeUsuarioAdministradorFragment
        loadFragment(RegistroAdministradorFragment())
    }
    fun irARegistroUsuario() {
        // Este método cargará el fragmento SeleccionDeUsuarioAdministradorFragment
        loadFragment(RegistroUsuarioFragment())
    }

    // Método para cargar el Fragmento de Registro Administrador
    fun showRegistroAdministradorFragment() {
        loadFragment(RegistroAdministradorFragment())
    }

    // Método para cargar el Fragmento de Registro Usuario
    fun showRegistroUsuarioFragment() {
        loadFragment(RegistroUsuarioFragment())
    }
    fun irASeleccionDeUsuarioAdministrador() {
        // Este método cargará el fragmento SeleccionDeUsuarioAdministradorFragment
        loadFragment(SeleccionTipoUsuarioFragment())
    }
    // Método para ir al Fragmento de Login
    fun irALogin() {
        showLoginFragment() // Cambia al fragmento de Login
    }
}
