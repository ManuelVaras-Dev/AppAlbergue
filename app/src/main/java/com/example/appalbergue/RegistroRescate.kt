
package com.example.appalbergue

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.model.Rescate

import android.util.Base64
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appalbergue.adapter.RescateAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream



class RegistroRescate : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtCelular: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var rescateAdapter: RescateAdapter
    private lateinit var buttonAgregar: Button
    private lateinit var buttonEditar: Button
    private lateinit var buttonEliminar: Button
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSelectLocation: Button // NUEVO botón para seleccionar ubicación
    private lateinit var imageView: ImageView
    private var rescateList: MutableList<Rescate> = mutableListOf()
    private var selectedRescate: Rescate? = null
    private var selectedImageBase64: String = ""
    private var selectedLocation: String = "" // Variable para guardar la ubicación seleccionada
    private lateinit var buttonExpandMap: ImageButton // Botón para expandir el mapa


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescate)

        edtNombre = findViewById(R.id.edtNombre)
        edtCelular = findViewById(R.id.edtCelular)
        edtCorreo = findViewById(R.id.edtCorreo)
        recyclerView = findViewById(R.id.recyclerView)
        buttonAgregar = findViewById(R.id.buttonAgregar)
        buttonEditar = findViewById(R.id.buttonEditar)
        buttonEliminar = findViewById(R.id.buttonEliminar)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSelectLocation = findViewById(R.id.buttonSelectLocation)
        imageView = findViewById(R.id.imageView)
        buttonExpandMap = findViewById(R.id.btnExpandMap)

        database = FirebaseDatabase.getInstance().getReference("personas")


        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rescateAdapter = RescateAdapter(rescateList) { persona -> onItemSelected(persona) }
        recyclerView.adapter = rescateAdapter

        buttonAgregar.setOnClickListener { addPersona() }
        buttonEditar.setOnClickListener { editPersona() }
        buttonEliminar.setOnClickListener { deletePersona() }
        buttonSelectImage.setOnClickListener { selectImageFromGallery() }
        buttonSelectLocation.setOnClickListener { openMap() } // NUEVO evento para abrir mapa
        val rescate = Rescate()
        buttonExpandMap.setOnClickListener { expandMap(rescate) } // NUEVO: Expandir mapa

        loadDataFromFirebase()

    }
    // Método para seleccionar una imagen desde la galería
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    // Método para expandir el mapa
    private fun expandMap(rescate: Rescate) {
        Toast.makeText(this, "Mostrando ubicación", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("persona", rescate) // Pasar objeto Persona con la ubicación
        startActivity(intent)
    }




    // NUEVO: Método para abrir Google Maps y seleccionar una ubicación
    private fun openMap() {
        val intent = Intent(this, MapActivity::class.java)

        // Si ya hay una ubicación guardada, pasarla a MapActivity
        if (selectedLocation.isNotEmpty()) {
            intent.putExtra("EXISTING_LOCATION", selectedLocation) // Ubicación guardada
        } else {
            intent.putExtra("USE_CURRENT_LOCATION", true) // Solicitar ubicación actual
        }

        startActivityForResult(intent, REQUEST_LOCATION_PICK)
    }


    // Recibir la imagen y ubicación seleccionada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                selectedImageBase64 = encodeImageToBase64(bitmap)
                imageView.setImageBitmap(bitmap) // Mostrar la imagen seleccionada
                Log.d("MainActivity", "Imagen seleccionada y convertida a Base64")
            }
        }

        if (requestCode == REQUEST_LOCATION_PICK && resultCode == RESULT_OK) {
            // Asegúrate de que las coordenadas se estén recibiendo correctamente
            selectedLocation = data?.getStringExtra("LOCATION") ?: ""

            if (selectedLocation.isNotEmpty()) {
                // Aquí verificamos que la ubicación no esté vacía
                Log.d("MainActivity", "Ubicación seleccionada: $selectedLocation")
                // Para separar latitud y longitud (si se pasan como una cadena)
                val locationParts = selectedLocation.split(",")
                if (locationParts.size == 2) {
                    val latitude = locationParts[0].toDoubleOrNull()
                    val longitude = locationParts[1].toDoubleOrNull()
                    if (latitude != null && longitude != null) {
                        Log.d("MainActivity", "Latitud: $latitude, Longitud: $longitude")
                    } else {
                        Log.e("MainActivity", "Las coordenadas no son válidas")
                    }
                }
            } else {
                Log.e("MainActivity", "No se ha recibido ninguna ubicación")
            }
        }
    }



    // Convertir Bitmap a Base64
    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Decodificar Base64 a Bitmap
    private fun decodeBase64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    // Agregar persona
    private fun addPersona() {
        val nombre = edtNombre.text.toString()
        val celular = edtCelular.text.toString()
        val correo = edtCorreo.text.toString()

        // Imprimir en consola los datos de selectedPersona
        //Log.d("Registrar Persona", " Nombre: ${nombre}, Celular: ${celular}, Correo: ${correo}, Imagen: ${selectedImageBase64}, Ubicación: ${selectedLocation}")


        // Verificar que todos los campos estén completos
        if (nombre.isEmpty() || celular.isEmpty() || correo.isEmpty() || selectedImageBase64.isEmpty() || selectedLocation.isEmpty()) {
            Toast.makeText(this, "Debes registrar todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = database.push().key ?: return

        // Crear objeto Persona con la imagen en Base64
        val rescate = Rescate(id, nombre, celular, correo, selectedImageBase64,selectedLocation)
        database.child(id).setValue(rescate)

        // Mostrar Toast de confirmación
        Toast.makeText(this, "Persona agregada correctamente", Toast.LENGTH_SHORT).show()

        // Restablecer campos y el placeholder de la imagen
        clearFields()
        imageView.setImageResource(R.drawable.place_holder_imagen)
    }

    // Editar persona
    private fun editPersona() {
        selectedRescate?.let {

            // Imprimir en consola los datos de selectedPersona
            //Log.d("EditPersona", "ID: ${it.id}, Nombre: ${it.nombre}, Celular: ${it.celular}, Correo: ${it.correo}, Ubicación: ${it.ubicacion}, ImagenBase64: ${it.imagenBase64}")

            val nombre = edtNombre.text.toString()
            val celular = edtCelular.text.toString()
            val correo = edtCorreo.text.toString()

            // Usar la imagen existente si no se selecciona una nueva

            val updatedImageBase64 = if (selectedImageBase64.isNotEmpty()) selectedImageBase64 else it.imagenBase64

            val updatedLocation = if (selectedLocation.isNotEmpty()) selectedLocation else it.ubicacion

            // Verificar que todos los campos estén completos
            if (nombre.isEmpty() || celular.isEmpty() || correo.isEmpty() || updatedImageBase64.isEmpty() || updatedLocation.isEmpty()) {
                Toast.makeText(this, "Debes registrar todos los campos", Toast.LENGTH_SHORT).show()
                return
            }

            // Crear objeto actualizado con imagen en Base64
            val updatedRescate = Rescate(it.id, nombre, celular, correo, updatedImageBase64,updatedLocation)
            database.child(it.id).setValue(updatedRescate)

            // Mostrar Toast de confirmación
            Toast.makeText(this, "Persona editada correctamente", Toast.LENGTH_SHORT).show()

            // Restablecer campos y el placeholder de la imagen
            clearFields()
            imageView.setImageResource(R.drawable.place_holder_imagen)
        } ?: run {
            Toast.makeText(this, "No se ha seleccionado ningún registro para editar", Toast.LENGTH_SHORT).show()
        }
    }

    // Eliminar persona
    private fun deletePersona() {

        if (selectedRescate == null) {
            // Si no hay persona seleccionada, mostrar un mensaje Toast
            Toast.makeText(this, "No se ha seleccionado ningún registro para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        selectedRescate?.let {
            database.child(it.id).removeValue()

            // Mostrar Toast de confirmación
            Toast.makeText(this, "Persona eliminada correctamente", Toast.LENGTH_SHORT).show()

            // Restablecer el valor de selectedPersona a null
            selectedRescate = null
            // Restablecer campos y el placeholder de la imagen
            clearFields()
            imageView.setImageResource(R.drawable.place_holder_imagen)
        } ?: run {
            Toast.makeText(this, "No se ha seleccionado ningún registro para eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    // Cargar datos desde Firebase
    private fun loadDataFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rescateList.clear()
                for (personaSnapshot in snapshot.children) {
                    val rescate = personaSnapshot.getValue(Rescate::class.java)
                    rescate?.let { rescateList.add(it) }
                }
                rescateAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }

    // Seleccionar un item del RecyclerView
    private fun onItemSelected(rescate: Rescate) {
        selectedRescate = rescate
        edtNombre.setText(rescate.nombre)
        edtCelular.setText(rescate.celular)
        edtCorreo.setText(rescate.correo)

        // Mostrar imagen en ImageView
        if (rescate.imagenBase64.isNotEmpty()) {
            val bitmap = decodeBase64ToImage(rescate.imagenBase64)
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.place_holder_imagen) // Reemplazar por placeholder
        }
    }

    // Limpiar campos de entrada y restablecer imagen
    private fun clearFields() {
        edtNombre.text.clear()
        edtCelular.text.clear()
        edtCorreo.text.clear()
        selectedImageBase64 = ""
        selectedLocation = ""
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        private const val REQUEST_LOCATION_PICK = 2 // NUEVO código de solicitud para ubicación
    }
}
