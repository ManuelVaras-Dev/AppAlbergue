package com.example.appalbergue

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import android.util.Base64

class RegistroLocal : AppCompatActivity() {

    private lateinit var buttonCancel: Button
    private lateinit var buttonSubirImagen: Button
    private lateinit var buttonRegistrarLocal: Button
    private lateinit var imageViewLocal: ImageView
    private lateinit var editTextLocal: EditText
    private lateinit var editTextDireccion: EditText
    private lateinit var editTextDistrito: EditText
    private lateinit var editTextResponsable: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextCorreo: EditText
    private lateinit var database: DatabaseReference

    private var selectedImageBase64: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_registrar)

        // Vincular vistas
        buttonCancel = findViewById(R.id.button_cancel)
        buttonSubirImagen = findViewById(R.id.button_subir_imagen)
        buttonRegistrarLocal = findViewById(R.id.button_registrar_local)
        imageViewLocal = findViewById(R.id.image_view_local)
        editTextLocal = findViewById(R.id.edittext_local)
        editTextDireccion = findViewById(R.id.edittext_direccion)
        editTextDistrito = findViewById(R.id.edittext_distrito)
        editTextResponsable = findViewById(R.id.edittext_responsable)
        editTextTelefono = findViewById(R.id.edittext_telefono)
        editTextCorreo = findViewById(R.id.edittext_correo)

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance().getReference("locales")

        // Configurar botones
        buttonCancel.setOnClickListener { finish() } // Cerrar la actividad al presionar "Cancelar"
        buttonSubirImagen.setOnClickListener { selectImageFromGallery() }
        buttonRegistrarLocal.setOnClickListener { registrarLocal() }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                selectedImageBase64 = encodeImageToBase64(bitmap)
                imageViewLocal.setImageBitmap(bitmap)
                imageViewLocal.visibility = ImageView.VISIBLE
            }
        }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun registrarLocal() {
        val local = editTextLocal.text.toString()
        val direccion = editTextDireccion.text.toString()
        val distrito = editTextDistrito.text.toString()
        val responsable = editTextResponsable.text.toString()
        val telefono = editTextTelefono.text.toString()
        val correo = editTextCorreo.text.toString()

        if (local.isEmpty() || direccion.isEmpty() || distrito.isEmpty() || responsable.isEmpty() || telefono.isEmpty() || correo.isEmpty() || selectedImageBase64.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val id = database.push().key ?: return
        val nuevoLocal = Local(id, local, direccion, distrito, responsable, telefono, correo, selectedImageBase64)

        database.child(id).setValue(nuevoLocal).addOnSuccessListener {
            Toast.makeText(this, "Local registrado con éxito", Toast.LENGTH_SHORT).show()
            clearFields()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al registrar el local", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        editTextLocal.text.clear()
        editTextDireccion.text.clear()
        editTextDistrito.text.clear()
        editTextResponsable.text.clear()
        editTextTelefono.text.clear()
        editTextCorreo.text.clear()
        imageViewLocal.setImageResource(0)
        imageViewLocal.visibility = ImageView.GONE
        selectedImageBase64 = ""
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    data class Local(
        val id: String,
        val local: String,
        val direccion: String,
        val distrito: String,
        val responsable: String,
        val telefono: String,
        val correo: String,
        val imagenBase64: String
    )
}