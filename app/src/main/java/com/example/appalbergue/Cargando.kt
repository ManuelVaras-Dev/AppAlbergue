package com.example.appalbergue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CargandoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.cargando, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aquí puedes agregar la lógica que quieras al fragmento
        // Por ejemplo, puedes iniciar un temporizador o mostrar un progreso

        // Simulación de carga (Ejemplo)
        view.postDelayed({
            // Aquí puedes redirigir al siguiente fragmento o actividad
            (activity as? MainActivity)?.irALogin() // Cambia a tu flujo deseado
        }, 2000) // Espera 2 segundos antes de cambiar
    }
}
