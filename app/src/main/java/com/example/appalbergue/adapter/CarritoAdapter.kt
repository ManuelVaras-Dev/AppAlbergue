package com.example.appalbergue.adapter

import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appalbergue.R
import com.example.appalbergue.model.CarritoItem
import com.google.firebase.database.DatabaseReference

class CarritoAdapter(
    private val carritoItems: List<CarritoItem>,
    private val carritoRef: DatabaseReference
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.product_image1)
        val productTitle: TextView = view.findViewById(R.id.product_title1)
        val productDescription: TextView = view.findViewById(R.id.product_description1)
        val productQuantity: TextView = view.findViewById(R.id.product_quantity1)
        val btnIncrementar: Button = view.findViewById(R.id.btnIncrementar)
        val btnDecrementar: Button = view.findViewById(R.id.btnDecrementar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = carritoItems[position]

        // Configurar datos en la interfaz
        holder.productTitle.text = item.nombre
        holder.productDescription.text = item.comentarios
        holder.productQuantity.text = item.cantidad.toString()

        // Mostrar imagen decodificada
        val imageBytes = Base64.decode(item.imagen, Base64.DEFAULT)
        Glide.with(holder.productImage.context)
            .load(imageBytes)
            .into(holder.productImage)

        // Acción para incrementar la cantidad
        holder.btnIncrementar.setOnClickListener {
            val nuevaCantidad = item.cantidad + 1
            actualizarCantidadEnFirebase(item.id, nuevaCantidad, holder.itemView.context)
        }

        // Acción para decrementar la cantidad
        holder.btnDecrementar.setOnClickListener {
            if (item.cantidad > 1) {
                val nuevaCantidad = item.cantidad - 1
                actualizarCantidadEnFirebase(item.id, nuevaCantidad, holder.itemView.context)
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "La cantidad no puede ser menor a 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return carritoItems.size
    }

    /**
     * Método para actualizar la cantidad en Firebase
     * @param productoId El ID del producto a actualizar
     * @param nuevaCantidad La nueva cantidad a establecer
     * @param context Contexto de la vista
     */
    private fun actualizarCantidadEnFirebase(
        productoId: String,
        nuevaCantidad: Int,
        context: Context
    ) {
        carritoRef.child("carrito").orderByChild("id").equalTo(productoId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (productoSnapshot in snapshot.children) {
                        productoSnapshot.ref.child("cantidad").setValue(nuevaCantidad)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Cantidad actualizada correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { error ->
                                Toast.makeText(
                                    context,
                                    "Error al actualizar cantidad: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Producto no encontrado en la base de datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(
                    context,
                    "Error al buscar producto: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Firebase", "Error al buscar producto: ${error.message}")
            }
    }
}
