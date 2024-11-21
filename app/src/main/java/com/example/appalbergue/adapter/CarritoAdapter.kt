package com.example.appalbergue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.R
import com.example.appalbergue.model.CarritoItem
import com.example.appalbergue.model.Producto

class CarritoAdapter(private var carritoItems: List<CarritoItem>) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    // Este método actualizará la lista de carritoItems cuando los datos cambien
    fun actualizarCarrito(carritoItems: List<CarritoItem>) {
        this.carritoItems = carritoItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val carritoItem = carritoItems[position]

        // Configurar los datos en los elementos de la vista
        holder.nombreProducto.text = carritoItem.nombre
        holder.comentariosProducto.text = carritoItem.comentarios
        holder.cantidadProducto.text = carritoItem.cantidad.toString()

        // Si tienes una imagen en base64, puedes decodificarla y mostrarla (opcional)
        // Si la imagen está en base64 y quieres mostrarla, puedes decodificarla y usar un ImageView
        // Si no es base64, se puede utilizar una URL para cargar la imagen usando Glide o Picasso.

        // Aquí se muestra solo el nombre y comentarios como ejemplo:
    }

    override fun getItemCount(): Int {
        return carritoItems.size
    }

    // ViewHolder para los elementos del carrito
    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.nombre)
        val comentariosProducto: TextView = itemView.findViewById(R.id.comentarios)
        val cantidadProducto: TextView = itemView.findViewById(R.id.cantidad)
        // Agrega más elementos si es necesario, como la imagen
    }
}
