package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.adapter.ProductoAdapter
import com.example.appalbergue.model.Producto
import com.google.firebase.database.*


class ListarProducto : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var editTextSearch: EditText
    private val productos = mutableListOf<Producto>()
    private val productosFiltrados = mutableListOf<Producto>() // Para buscar productos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.producto_listar)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productoAdapter = ProductoAdapter(productosFiltrados) // Mostrar la lista filtrada
        recyclerView.adapter = productoAdapter

        // Configurar EditText para búsqueda
        editTextSearch = findViewById(R.id.edit_text_search)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarProductos(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Configurar botón "Agregar Producto"
        val buttonAddProduct: Button = findViewById(R.id.button_add_product)
        buttonAddProduct.setOnClickListener {
            val intent = Intent(this, RegistroProducto::class.java)
            startActivity(intent)
        }

        // Cargar productos desde Firebase
        cargarProductosDesdeFirebase()
    }

    private fun cargarProductosDesdeFirebase() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("productos")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productos.clear() // Limpiar lista principal
                for (productoSnapshot in snapshot.children) {
                    val producto = productoSnapshot.getValue(Producto::class.java)
                    if (producto != null) {
                        productos.add(producto)
                    }
                }
                filtrarProductos(editTextSearch.text.toString()) // Aplicar filtro actual
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }

    private fun filtrarProductos(query: String) {
        productosFiltrados.clear()
        if (query.isEmpty()) {
            productosFiltrados.addAll(productos)
        } else {
            productosFiltrados.addAll(
                productos.filter { it.nombre.contains(query, ignoreCase = true) }
            )
        }
        productoAdapter.notifyDataSetChanged()
    }
}