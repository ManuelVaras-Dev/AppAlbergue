package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appalbergue.adapter.CategoriasAdapter
import com.example.appalbergue.model.CarritoItem
import com.example.appalbergue.model.Producto
import com.google.firebase.database.*

class ProductosDonacion : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriasAdapter: CategoriasAdapter
    private lateinit var database: DatabaseReference // Referencia a Firebase
    private lateinit var buscador: EditText // Campo de búsqueda
    private val productosList = mutableListOf<Producto>() // Lista original de productos
    private val productosFiltrados = mutableListOf<Producto>() // Lista filtrada
    private val carrito = mutableListOf<Producto>() // Lista del carrito

    // Botones para las categorías
    private lateinit var btnTodas: Button
    private lateinit var btnAlimentos: Button
    private lateinit var btnMedicinas: Button
    private lateinit var btnAccesorios: Button
    // Imagen para ir al carrito
    private lateinit var irACarrito: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categorias_menu)

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("productos")

        // Inicializar el EditText para el buscador
        buscador = findViewById(R.id.buscador)

        // Inicializar botones
        btnTodas = findViewById(R.id.btn_todas)
        btnAlimentos = findViewById(R.id.btn_alimentos)
        btnMedicinas = findViewById(R.id.btn_medicinas)
        btnAccesorios = findViewById(R.id.btn_accesorios)
        irACarrito = findViewById(R.id.irACarrito) // Inicializar la imagen

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Configurar el Adapter con callback para agregar al carrito
        categoriasAdapter = CategoriasAdapter(productosFiltrados) { producto ->
            agregarAlCarrito(producto)
        }
        recyclerView.adapter = categoriasAdapter

        // Cargar datos desde Firebase
        cargarDatosDesdeFirebase()

        // Agregar listener para el buscador
        configurarBuscador()

        // Configurar listeners para los botones de categoría
        configurarBotones()

        // Agregar listener para la imagen irACarrito
        irACarrito.setOnClickListener {
            // Aquí puedes redirigir al layout "categorias_carrito"
            val intent = Intent(this, ProductosDonacionCarrito::class.java)
            startActivity(intent)
        }
    }

    // Método para agregar productos al carrito y guardarlos en Firebase
    private fun agregarAlCarrito(producto: Producto) {
        carrito.add(producto) // Agregar producto al carrito
        Log.d("ProductosDonacion", "Producto agregado al carrito: ${producto.nombre}")

        // Guardar el carrito en Firebase
        val carritoRef = FirebaseDatabase.getInstance().getReference("carrito")

        val key = carritoRef.push().key ?: return // Si no se puede generar un key, retornamos

        // Crear un nuevo objeto de carrito para Firebase con los campos requeridos
        val carritoItem = CarritoItem(
            id = key,
            nombre = producto.nombre,
            comentarios = producto.sugerencia,
            imagen = producto.imagenBase64,
            cantidad = 1 // Aquí puedes manejar la cantidad más adelante si lo deseas
        )

        // Usar un push para agregar un nuevo elemento al carrito de Firebase
        carritoRef.push().setValue(carritoItem).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("ProductosDonacion", "Producto guardado en el carrito de Firebase")
            } else {
                Log.e("ProductosDonacion", "Error al guardar el producto en Firebase: ${it.exception?.message}")
            }
        }
    }

    private fun cargarDatosDesdeFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productosList.clear() // Limpiar la lista original
                productosFiltrados.clear() // Limpiar la lista filtrada también

                for (productoSnapshot in snapshot.children) {
                    val producto = productoSnapshot.getValue(Producto::class.java)
                    if (producto != null) {
                        productosList.add(producto)
                    }
                }

                // Mostrar todos los productos al inicio
                productosFiltrados.addAll(productosList)
                categoriasAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductosDonacion", "Error al cargar datos: ${error.message}")
            }
        })
    }

    private fun configurarBuscador() {
        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textoBusqueda = s.toString().lowercase() // Texto del buscador
                filtrarProductos(textoBusqueda)
            }
        })
    }

    private fun filtrarProductos(texto: String) {
        productosFiltrados.clear() // Limpiar la lista filtrada

        if (texto.isEmpty()) {
            // Si el buscador está vacío, mostrar todos los productos
            productosFiltrados.addAll(productosList)
        } else {
            // Filtrar los productos que contengan el texto en el nombre o estado
            for (producto in productosList) {
                if ((producto.nombre.lowercase().contains(texto)) ||
                    (producto.estado.lowercase().contains(texto))) {
                    productosFiltrados.add(producto)
                }
            }
        }

        // Notificar al adapter que los datos han cambiado
        categoriasAdapter.notifyDataSetChanged()
    }

    private fun configurarBotones() {
        // Configurar los listeners para cada botón
        btnTodas.setOnClickListener { actualizarBotones(btnTodas); filtrarPorCategoria("todas") }
        btnAlimentos.setOnClickListener { actualizarBotones(btnAlimentos); filtrarPorCategoria("alimentos") }
        btnMedicinas.setOnClickListener { actualizarBotones(btnMedicinas); filtrarPorCategoria("medicinas") }
        btnAccesorios.setOnClickListener { actualizarBotones(btnAccesorios); filtrarPorCategoria("accesorios") }
    }

    private fun actualizarBotones(botonSeleccionado: Button) {
        // Lista de botones
        val botones = listOf(btnTodas, btnAlimentos, btnMedicinas, btnAccesorios)

        // Iterar por todos los botones
        for (boton in botones) {
            if (boton == botonSeleccionado) {
                // Estilo para el botón seleccionado
                boton.setTextColor(ContextCompat.getColor(this, R.color.blanco)) // Color blanco
                boton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.verde) // Fondo verde
            } else {
                // Estilo para los botones no seleccionados
                boton.setTextColor(ContextCompat.getColor(this, R.color.negroclaro)) // Color negro claro
                boton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grisclaro) // Fondo gris claro
            }
        }
    }

    private fun filtrarPorCategoria(categoria: String) {
        // Filtrar productos según la categoría seleccionada
        productosFiltrados.clear()

        if (categoria == "todas") {
            productosFiltrados.addAll(productosList)
        } else {
            // Filtrar productos por categoría
            for (producto in productosList) {
                if (producto.categoria.lowercase() == categoria.lowercase()) {
                    productosFiltrados.add(producto)
                }
            }
        }

        // Notificar al adapter que los datos han cambiado
        categoriasAdapter.notifyDataSetChanged()
    }
}
