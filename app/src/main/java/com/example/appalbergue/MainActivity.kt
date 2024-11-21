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

}
