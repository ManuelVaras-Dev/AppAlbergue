package com.example.appalbergue

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.Manifest
import android.util.Log
import com.example.appalbergue.model.Rescate

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var selectedLocation: LatLng
    private var savedLocation: LatLng? = null // Ubicación guardada
    private val defaultZoom = 15f // Nivel de zoom por defecto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Leer la ubicación pasada desde el Adapter (de la base de datos)
        // Leer la ubicación desde el Adapter (base de datos) o desde MainActivity (ubicación guardada)
        intent.extras?.let {
            // Validar ubicación desde la base de datos (Adapter)
            val rescate: Rescate? = it.getParcelable("persona")
            rescate?.let { persona ->
                val ubicacion = persona.ubicacion.split(",")
                if (ubicacion.size == 2) {
                    savedLocation = LatLng(ubicacion[0].toDouble(), ubicacion[1].toDouble())
                    Log.d("MapActivity", "Ubicación obtenida 1 de la base de datos: Lat=${ubicacion[0]}, Lng=${ubicacion[1]}")
                }
            }

            // Si no se encontró ubicación en la base de datos, validar la ubicación pasada desde MainActivity
            if (savedLocation == null) {
                val savedLocationString = it.getString("EXISTING_LOCATION", "")
                if (savedLocationString.isNotEmpty()) {
                    val coordinates = savedLocationString.split(",")
                    savedLocation = LatLng(coordinates[0].toDouble(), coordinates[1].toDouble())
                    Log.d("MapActivity", "Ubicación obtenida 2 desde MainActivity: Lat=${coordinates[0]}, Lng=${coordinates[1]}")
                }
            }
        }


        // Configurar el fragmento del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configurar botón para cancelar la selección
        findViewById<Button>(R.id.btnCancelLocation).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish() // Finaliza la actividad sin enviar datos
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Mostrar la ubicación guardada si existe
        savedLocation?.let {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, defaultZoom))
            mMap.addMarker(MarkerOptions().position(it).title("Ubicación de ..."))
            selectedLocation = it // Guardar como ubicación seleccionada
        } ?: run {
            // Si no hay ubicación guardada, obtener la ubicación actual
            getCurrentLocation()
        }

        // Configurar listener para tocar el mapa y seleccionar una nueva ubicación
        mMap.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            mMap.clear() // Limpiar marcadores anteriores
            mMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))

            Log.d("MapActivity", "Ubicación seleccionada: Lat=${latLng.latitude}, Lng=${latLng.longitude}")
        }

        // Configurar botón para confirmar ubicación
        findViewById<Button>(R.id.btnConfirmLocation).setOnClickListener {
            if (::selectedLocation.isInitialized) {
                val locationString = "${selectedLocation.latitude},${selectedLocation.longitude}" // Formatear la ubicación
                val resultIntent = Intent().apply {
                    putExtra("LOCATION", locationString) // Enviar como cadena
                }
                setResult(RESULT_OK, resultIntent)

                Log.d("MapActivity", "Ubicación confirmada: $locationString")
                finish() // Finalizar la actividad y devolver datos
            } else {
                Toast.makeText(this, "Por favor selecciona una ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Obtener la última ubicación conocida
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                selectedLocation = currentLatLng // Guardar la ubicación actual
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, defaultZoom))
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Ubicación actual"))

                Log.d("MapActivity", "Ubicación actual: Lat=${location.latitude}, Lng=${location.longitude}")
            } else {
                Toast.makeText(this, "No se pudo obtener tu ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        // Llamar a la implementación de la superclase
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
