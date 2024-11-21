package com.example.appalbergue

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

import java.io.ByteArrayOutputStream

class RegistroProducto : AppCompatActivity() {

    private lateinit var buttonCancel: Button
    private lateinit var buttonSubirImagen: Button
    private lateinit var imageViewProducto: ImageView
    private lateinit var spinnerCategoria: Spinner
    private lateinit var spinnerSubcategoria: Spinner
    private lateinit var editTextProducto: EditText
    private lateinit var editTextSugerencia: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var buttonRegistrarProducto: Button

    private var selectedImageBase64: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.producto_registrar)

        buttonCancel = findViewById(R.id.button_cancel)
        buttonSubirImagen = findViewById(R.id.button_subir_imagen)
        imageViewProducto = findViewById(R.id.image_view_producto)
        spinnerCategoria = findViewById(R.id.spinner_categoria)
        spinnerSubcategoria = findViewById(R.id.spinner_subcategoria)
        editTextProducto = findViewById(R.id.edittext_producto)
        editTextSugerencia = findViewById(R.id.edittext_sugerencia)
        spinnerEstado = findViewById(R.id.spinner_estado)
        buttonRegistrarProducto = findViewById(R.id.button_registrar_producto)

        configurarSpinners()
        buttonCancel.setOnClickListener { finish() }
        buttonSubirImagen.setOnClickListener { seleccionarImagenDesdeGaleria() }
        buttonRegistrarProducto.setOnClickListener { registrarProductoEnFirebase() }
    }

    private fun configurarSpinners() {
        val categorias = listOf("Alimentos", "Medicinas", "Accesorios")
        val subcategorias = listOf("Perro", "Gato")
        val estados = listOf("Con Stock", "Sin Stock")
        spinnerCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spinnerSubcategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subcategorias)
        spinnerEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
    }

    private fun seleccionarImagenDesdeGaleria() {
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
                imageViewProducto.setImageBitmap(bitmap)
                imageViewProducto.visibility = ImageView.VISIBLE
            }
        }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun registrarProductoEnFirebase() {
        val nombre = editTextProducto.text.toString()
        val sugerencia = editTextSugerencia.text.toString()
        val categoria = spinnerCategoria.selectedItem?.toString()
        val subcategoria = spinnerSubcategoria.selectedItem?.toString()
        val estado = spinnerEstado.selectedItem?.toString()

        if (nombre.isEmpty() || sugerencia.isEmpty() || categoria == null || subcategoria == null || estado == null || selectedImageBase64.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val productoData = mapOf(
            "nombre" to nombre,
            "sugerencia" to sugerencia,
            "categoria" to categoria,
            "subcategoria" to subcategoria,
            "estado" to estado,
            "imagenBase64" to selectedImageBase64
        )

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("productos")

        ref.push().setValue(productoData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Producto registrado", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error al registrar el producto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limpiarCampos() {
        editTextProducto.text.clear()
        editTextSugerencia.text.clear()
        spinnerCategoria.setSelection(0)
        spinnerSubcategoria.setSelection(0)
        spinnerEstado.setSelection(0)
        imageViewProducto.setImageResource(0)
        imageViewProducto.visibility = ImageView.GONE
        selectedImageBase64 = ""
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}