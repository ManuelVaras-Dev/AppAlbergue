package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.adapter.CarritoAdapter
import com.example.appalbergue.model.CarritoItem
import com.google.firebase.database.*

class ProductosDonacionCarrito : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private val carritoItems = mutableListOf<CarritoItem>()
    private lateinit var carritoRef: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categorias_carrito) // Layout principal con un RecyclerView

        // Inicializar la referencia de Firebase antes de usarla
        carritoRef = FirebaseDatabase.getInstance().getReference("carrito")

        // Inicialización de RecyclerView y Adapter
        recyclerView = findViewById(R.id.recyclerViewCarrito)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pasar carritoItems y carritoRef al adaptador
        carritoAdapter = CarritoAdapter(carritoItems, carritoRef)
        recyclerView.adapter = carritoAdapter

        // Cargar datos del carrito desde Firebase
        cargarCarritoDesdeFirebase()

        // Configurar el botón de regreso
        val btnRegresar: Button = findViewById(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            regresarAPantallaAnterior()
        }

        // Configurar el botón de regreso
        val btnContinuar: Button = findViewById(R.id.btnContinuar)
        btnContinuar.setOnClickListener {
            continuarAPantallaSiguiente()
        }
    }

    private fun cargarCarritoDesdeFirebase() {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                carritoItems.clear() // Limpiar la lista antes de agregar los nuevos elementos
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(CarritoItem::class.java)
                    if (item != null && itemSnapshot.key != null) {
                        item.id = itemSnapshot.key!! // Aseguramos que cada producto tenga un ID
                        carritoItems.add(item)
                    }
                }
                if (carritoItems.isEmpty()) {
                    mostrarMensaje("El carrito está vacío.")
                }
                carritoAdapter.notifyDataSetChanged() // Notificar al adapter de los cambios
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductosDonacionCarrito", "Error al cargar el carrito: ${error.message}")
                mostrarMensaje("No se pudo cargar el carrito. Intenta nuevamente.")
            }
        }

        carritoRef.addValueEventListener(valueEventListener)
    }


    private fun actualizarCantidadEnFirebase(productoId: String, nuevaCantidad: Int, position: Int) {
        val productoRef = carritoRef.child(productoId) // Obtener la referencia correcta del producto

        // Actualizamos solo la cantidad del producto
        val updates = mapOf<String, Any>("cantidad" to nuevaCantidad)

        productoRef.updateChildren(updates) // Actualizamos en Firebase
            .addOnSuccessListener {
                // Actualizamos la cantidad localmente en la lista
                carritoItems[position].cantidad = nuevaCantidad
                carritoAdapter.notifyItemChanged(position) // Notificar al adaptador de la actualización
                mostrarMensaje("Cantidad actualizada a $nuevaCantidad") // Mostrar mensaje de éxito
            }
            .addOnFailureListener { error ->
                mostrarMensaje("Error al actualizar cantidad: ${error.message}") // Mostrar mensaje de error
            }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun regresarAPantallaAnterior() {
        val intent = Intent(this, ProductosDonacion::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }

    private fun continuarAPantallaSiguiente() {
        val intent = Intent(this, DonacionRegistro::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remover el listener para evitar fugas de memoria
        carritoRef.removeEventListener(valueEventListener)
    }
}
