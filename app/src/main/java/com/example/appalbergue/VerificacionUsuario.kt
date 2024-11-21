package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class VerificacionUsuarioragment : Fragment() {

    private lateinit var etCodigoVerificacion: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verificacion_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        etCodigoVerificacion = view.findViewById(R.id.etCodigoVerificacion)
        val btnVerificar = view.findViewById<Button>(R.id.verify_button)

        btnVerificar.setOnClickListener {
            verificarCodigo()
        }
    }

    private fun verificarCodigo() {
        val codigo = etCodigoVerificacion.text.toString().trim()

        // Validar el código ingresado
        if (codigo.length != 6) {
            Toast.makeText(requireContext(), "El código debe tener 6 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el usuario actual de Firebase
        val user = auth.currentUser

        if (user != null && user.isEmailVerified) {
            // Si el correo está verificado
            Toast.makeText(requireContext(), "Verificación exitosa. Redirigiendo...", Toast.LENGTH_SHORT).show()

            // Redirigir al login
            val intent = Intent(activity, LoginActivity::class.java)  // Asegúrate de que LoginActivity sea la actividad correcta
            startActivity(intent)
            activity?.finish() // Finalizar la actividad que contiene el fragmento
        } else {
            // Si el correo no está verificado
            Toast.makeText(requireContext(), "El correo no está verificado. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
        }
    }
}
