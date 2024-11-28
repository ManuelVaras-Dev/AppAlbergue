package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appalbergue.model.Donacion
import com.google.firebase.firestore.FirebaseFirestore

class DonacionRegistro : AppCompatActivity() {

    private lateinit var donanteNombre: EditText
    private lateinit var donanteCorreo: EditText
    private lateinit var donanteCelular: EditText
    private lateinit var donanteDireccion: EditText
    private lateinit var albergueNombre: Spinner
    private lateinit var albergueEncargado: EditText
    private lateinit var albergueCelularEncargado: EditText
    private lateinit var albergueDireccionEncargado: EditText
    private lateinit var formaEnvio: RadioGroup

    private lateinit var db: FirebaseFirestore

    // Variable to hold the selected shelter name
    private var selectedShelter: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donacion_registro)

        // Initialize UI components
        donanteNombre = findViewById(R.id.donante_nombre)
        donanteCorreo = findViewById(R.id.donante_correo)
        donanteCelular = findViewById(R.id.donante_celular)
        donanteDireccion = findViewById(R.id.donante_direccion)
        albergueNombre = findViewById(R.id.albergue_nombre)
        albergueEncargado = findViewById(R.id.albergue_encargado)
        albergueCelularEncargado = findViewById(R.id.albergue_celular_encargado)
        albergueDireccionEncargado = findViewById(R.id.albergue_direccion_encargado)
        formaEnvio = findViewById(R.id.forma_envio)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()

        // Setting up the Spinner with shelter options
        setupSpinner()

        // Find the button by its ID and set the onClick listener
        val continuarButton: Button = findViewById(R.id.donate_continuar)
        continuarButton.setOnClickListener {
            if (validateFields()) {
                saveDonacion() // Call save function when the button is clicked
                redirectToExitoza()
            }

        }
    }

    // Function to populate Spinner with shelter options
    private fun setupSpinner() {
        val shelters = listOf("Albergue el Milagro", "Albergue Huanchaco", "Albergue Laredo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, shelters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        albergueNombre.adapter = adapter

        // Set an item selected listener for the Spinner
        albergueNombre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedShelter = shelters[position] // Save the selected shelter
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing if no item is selected
            }
        }
    }

    // Function to validate input fields before saving
    private fun validateFields(): Boolean {
        return when {
            donanteNombre.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el nombre del donante", Toast.LENGTH_SHORT).show()
                false
            }
            donanteCorreo.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el correo del donante", Toast.LENGTH_SHORT).show()
                false
            }
            donanteCelular.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el celular del donante", Toast.LENGTH_SHORT).show()
                false
            }
            donanteDireccion.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese la dirección del donante", Toast.LENGTH_SHORT).show()
                false
            }
            selectedShelter.isEmpty() -> {
                Toast.makeText(this, "Por favor seleccione un albergue", Toast.LENGTH_SHORT).show()
                false
            }
            albergueEncargado.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el nombre del encargado", Toast.LENGTH_SHORT).show()
                false
            }
            albergueCelularEncargado.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el celular del encargado", Toast.LENGTH_SHORT).show()
                false
            }
            albergueDireccionEncargado.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor ingrese la dirección del encargado", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    // Function to get the selected donation method from the RadioGroup
    private fun getFormaEnvio(): String {
        return when (formaEnvio.checkedRadioButtonId) {
            R.id.envio_donante -> "Donante"
            R.id.envio_albergue -> "Albergue"
            else -> ""
        }
    }

    // Function to save the donation data into Firestore
    private fun saveDonacion() {
        // Obtener los datos de los campos
        val donanteNombre = donanteNombre.text.toString()
        val donanteCorreo = donanteCorreo.text.toString()
        val donanteCelular = donanteCelular.text.toString()
        val donanteDireccion = donanteDireccion.text.toString()
        val albergueEncargado = albergueEncargado.text.toString()
        val albergueCelularEncargado = albergueCelularEncargado.text.toString()
        val albergueDireccionEncargado = albergueDireccionEncargado.text.toString()
        val formaEnvio = getFormaEnvio() // Esta función debe retornar el tipo de envío

        // Crear un mapa de los datos para guardar en Firestore
        val donacionData = mapOf(
            "donanteNombre" to donanteNombre,
            "donanteCorreo" to donanteCorreo,
            "donanteCelular" to donanteCelular,
            "donanteDireccion" to donanteDireccion,
            "albergueEncargado" to albergueEncargado,
            "albergueCelularEncargado" to albergueCelularEncargado,
            "albergueDireccionEncargado" to albergueDireccionEncargado,
            "formaEnvio" to formaEnvio
        )

        // Obtener la instancia de Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Referencia a la colección de donaciones
        val donacionesRef = db.collection("donaciones")

        // Agregar los datos a Firestore
        donacionesRef.add(donacionData)
            .addOnSuccessListener {
                Toast.makeText(this, "Donación registrada correctamente", Toast.LENGTH_SHORT).show()
                redirectToExitoza() // Redirigir a DonacionExitoza después de guardar exitosamente
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar la donación", Toast.LENGTH_SHORT).show()
                Log.e("DonacionRegistro", "Error al guardar donación", e)
            }
    }

    // Function to redirect to the DonacionExitoza activity
    private fun redirectToExitoza() {
        val intent = Intent(this, DonacionExitoza::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent the user from going back to it
    }
}
