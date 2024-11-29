package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menuprincipal)

        // Configurar acciones específicas para cada botón
        findViewById<Button>(R.id.btnOpcion1).setOnClickListener {
            val intent = Intent(this, ListarProducto::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOpcion2).setOnClickListener {
            val intent = Intent(this, ProductosDonacion::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOpcion3).setOnClickListener {
            val intent = Intent(this, ListarLocal::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOpcion4).setOnClickListener {
            val intent = Intent(this, RegistroRescate::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }
    }
}
