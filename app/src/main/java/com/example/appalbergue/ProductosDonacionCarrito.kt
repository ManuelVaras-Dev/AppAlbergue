package com.example.appalbergue

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.adapter.CarritoAdapter
import com.example.appalbergue.model.CarritoItem
import com.example.appalbergue.model.Producto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductosDonacionCarrito : AppCompatActivity() {

    private lateinit var recyclerViewCarrito: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private val carrito = mutableListOf<CarritoItem>() // Lista de items del carrito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categorias_carrito)

        // Inicializar RecyclerView
        recyclerViewCarrito = findViewById(R.id.recyclerViewCarrito)
        recyclerViewCarrito.layoutManager = LinearLayoutManager(this)

        // Recibir la lista del carrito desde la actividad anterior
        val carritoItems = intent.getSerializableExtra("carrito") as? List<CarritoItem>

        // Verificar si recibimos los datos correctamente
        if (carritoItems != null && carritoItems.isNotEmpty()) {
            carrito.addAll(carritoItems)
        } else {
            Log.d("ProductosDonacionCarrito", "No se recibieron productos o la lista está vacía")
        }

        // Configurar el adapter con la lista recibida
        carritoAdapter = CarritoAdapter(carrito)
        recyclerViewCarrito.adapter = carritoAdapter

        // Verificar si la lista está vacía
        Log.d("ProductosDonacionCarrito", "Carrito contiene ${carrito.size} items")

        // Si deseas cargar los datos de Firebase, aquí puedes hacerlo también
        // cargarCarritoDesdeFirebase() // Llama a esta función si necesitas cargar los datos desde Firebase
    }

    // Método opcional para cargar el carrito desde Firebase
    private fun cargarCarritoDesdeFirebase() {
        val carritoRef = FirebaseDatabase.getInstance().getReference("carrito")

        carritoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val carritoItems = mutableListOf<CarritoItem>()
                for (dataSnapshot in snapshot.children) {
                    val carritoItem = dataSnapshot.getValue(CarritoItem::class.java)
                    carritoItem?.let {
                        carritoItems.add(it)
                    }
                }
                // Actualizar la lista de carrito y el adaptador
                carrito.clear()
                carrito.addAll(carritoItems)
                carritoAdapter.notifyDataSetChanged() // Actualizar la vista
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Carrito", "Error al cargar los datos de Firebase: ${error.message}")
            }
        })
    }
}
