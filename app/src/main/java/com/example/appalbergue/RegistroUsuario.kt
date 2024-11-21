package com.example.crudconfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appalbergue.LoginActivity
import com.example.appalbergue.R
import com.example.crudconfirebase.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtApellido: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var edtUsuario: EditText
    private lateinit var edtContraseña: EditText
    private lateinit var buttonRegistrar: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuario)

        // Asignar los valores de los EditText según los IDs del XML
        edtNombre = findViewById(R.id.etNombre)
        edtApellido = findViewById(R.id.etApellido)
        edtCorreo = findViewById(R.id.etCorreo)
        edtUsuario = findViewById(R.id.etUsuario)
        edtContraseña = findViewById(R.id.etContraseña)
        buttonRegistrar = findViewById(R.id.btnRegistrar)

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance().getReference("Usuarios")
        auth = FirebaseAuth.getInstance()

        // Configurar el botón de registro
        buttonRegistrar.setOnClickListener {
            registerPersona()
        }
    }

    // Función para registrar la persona en Firebase
    private fun registerPersona() {
        val nombre = edtNombre.text.toString()
        val apellido = edtApellido.text.toString()
        val correo = edtCorreo.text.toString()
        val usuario = edtUsuario.text.toString()
        val contraseña = edtContraseña.text.toString()

        // Verificar que todos los campos estén completos
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrar al usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Obtener el ID del usuario creado
                    val id = auth.currentUser?.uid ?: return@addOnCompleteListener

         // Guardar datos adicionales en Realtime Database
                    val persona = Usuario(id, nombre, apellido, correo, usuario, contraseña)
                    database.child(id).setValue(persona)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            clearFields()
                            // Redirigir al login
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Error en Firebase Authentication
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Limpiar los campos del formulario después de un registro exitoso
    private fun clearFields() {
        edtNombre.text.clear()
        edtApellido.text.clear()
        edtCorreo.text.clear()
        edtUsuario.text.clear()
        edtContraseña.text.clear()
    }
}
