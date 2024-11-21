package com.example.tuapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appalbergue.R

class RecoveryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.recuperaciondecuenta, container, false)

        // Configurar los elementos
        val emailField: EditText = view.findViewById(R.id.email_field)
        val sendRecoveryButton: Button = view.findViewById(R.id.btnSendRecoveryLink)

        sendRecoveryButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            if (email.isNotEmpty()) {
                Toast.makeText(activity, "Enlace de recuperación enviado a: $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Por favor, ingresa tu correo electrónico.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
