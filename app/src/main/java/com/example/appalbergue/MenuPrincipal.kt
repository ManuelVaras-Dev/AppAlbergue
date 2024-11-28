package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menuprincipal)

        // Referencias dinámicas a los botones
        val buttonIds = listOf(
            R.id.btnOpcion1,
            R.id.btnOpcion2,
            R.id.btnOpcion3,
            R.id.btnOpcion4
        )

        // Configurar acciones para cada botón
        buttonIds.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
