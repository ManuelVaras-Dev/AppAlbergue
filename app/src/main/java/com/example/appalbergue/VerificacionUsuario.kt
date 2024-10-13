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

class VerificacionUsuarioragment : Fragment() {

    private lateinit var etCodigoVerificacion: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verificacion_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // Aquí debes implementar la lógica para verificar el código
        if (codigo == "123456") { // Cambia esto por tu lógica de verificación real
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish() // Opcional: cerrar la actividad actual
        } else {
            Toast.makeText(requireContext(), "Código incorrecto. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
        }
    }
}
