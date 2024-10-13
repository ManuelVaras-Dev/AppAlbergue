package com.example.appalbergue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento de login
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a los botones de la UI
        val crearCuentaButton: Button = view.findViewById(R.id.crear_button)

        // Configurar la acción al hacer clic en el botón "Crear Cuenta"
        crearCuentaButton.setOnClickListener {
            // Navegar al Fragmento de Selección de Usuario Administrador
            (activity as? MainActivity)?.irASeleccionDeUsuarioAdministrador()
        }
    }
}
