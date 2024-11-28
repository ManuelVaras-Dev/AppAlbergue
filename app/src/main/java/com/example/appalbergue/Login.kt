package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Configura el layout de la actividad

        // Inicializar la instancia de Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtener referencias a los campos de entrada y botones de la UI
        val editTextCorreo: EditText = findViewById(R.id.username)
        val editTextContraseña: EditText = findViewById(R.id.password)
        val iniciarsesion: Button = findViewById(R.id.btnIniciarSesion)
        val crearCuentaButton: Button = findViewById(R.id.crear_button)

        // Configurar la acción al hacer clic en el botón "Iniciar Sesión"
        iniciarsesion.setOnClickListener {
            val correo = editTextCorreo.text.toString()
            val contraseña = editTextContraseña.text.toString()

            // Verificar las credenciales en Firebase
            verificarInicioSesion(correo, contraseña)
        }

        // Configurar la acción al hacer clic en el botón "Crear Cuenta"
        crearCuentaButton.setOnClickListener {
            val intent = Intent(this, com.example.appalbergue.RegistroUsuarioMainActivity::class.java)
            startActivity(intent)
        }
    }

    // Método para verificar las credenciales en Firebase
    private fun verificarInicioSesion(correo: String, contraseña: String) {
        auth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    // Redirigir a la pantalla principal
                    val intent = Intent(this, MenuPrincipalActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra la actividad de login para que no se pueda volver atrás
                } else {
                    // Si las credenciales no son correctas
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
