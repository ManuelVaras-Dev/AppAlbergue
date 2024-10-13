package com.example.appalbergue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class SeleccionTipoUsuarioFragment : Fragment() {

    private lateinit var btnNuevoUsuario: Button
    private lateinit var btnNuevoAdministrador: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.seleccion_tipo_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enlazar los botones con los IDs definidos en el layout
        btnNuevoUsuario = view.findViewById(R.id.btn_usuario_nuevo)
        btnNuevoAdministrador = view.findViewById(R.id.btn_administrador_nuevo)

        // Evento de click en "Soy Nuevo Usuario"
        btnNuevoUsuario.setOnClickListener {
            // Navega al fragmento de registro de usuario
            (activity as? MainActivity)?.irARegistroUsuario()
        }

        // Evento de click en "Nuevo Administrador"
        btnNuevoAdministrador.setOnClickListener {
            // Navega al fragmento de registro de administrador
            (activity as? MainActivity)?.irARegistroAdministrador()
        }
    }
}
