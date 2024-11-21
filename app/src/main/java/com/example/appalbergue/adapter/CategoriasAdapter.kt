package com.example.appalbergue.adapter

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
import com.example.appalbergue.R
import com.example.appalbergue.model.Producto

class CategoriasAdapter(
    private val productos: List<Producto>,
    private val onAgregarCarrito: (Producto) -> Unit // Callback para agregar al carrito
) : RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val producto = productos[position]

        // Setear los datos en los TextViews e ImageView
        holder.titulo.text = producto.nombre
        holder.comentarios.text = producto.sugerencia

        // Verificar el estado y ocultar la etiqueta si contiene "CON STOCK"
        if (producto.estado.equals("Con Stock", ignoreCase = true)) {
            holder.estado.text = ""
        } else {
            holder.estado.text = producto.estado
        }

        // Convertir la imagen Base64 a Bitmap y setearla en el ImageView
        try {
            val decodedString = Base64.decode(producto.imagenBase64, Base64.DEFAULT)
            val bitmap = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            holder.imagen.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("CategoriasAdapter", "Error al decodificar imagen: ${e.message}")
        }

        // Configurar el listener del botón "Agregar al carrito"
        holder.btnAgregarCarrito.setOnClickListener {
            onAgregarCarrito(producto)
            Toast.makeText(holder.itemView.context, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productos.size

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.titulo)
        val comentarios: TextView = itemView.findViewById(R.id.comentarios)
        val estado: TextView = itemView.findViewById(R.id.estado)
        val imagen: ImageView = itemView.findViewById(R.id.imagen)
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }
}
