package com.example.appalbergue.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.R
import com.example.appalbergue.model.Producto

class ProductoAdapter(private val productos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.producto_btn_editar, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = productos.size

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewProducto: ImageView = itemView.findViewById(R.id.image_view_producto)
        private val textViewProducto: TextView = itemView.findViewById(R.id.text_view_producto)
        private val textViewSugerencia: TextView = itemView.findViewById(R.id.text_view_sugerencia)
        private val textViewEstado: TextView = itemView.findViewById(R.id.text_view_estado)

        fun bind(producto: Producto) {
            // Decodificar la imagen en base64
            val imageBytes = Base64.decode(producto.imagenBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageViewProducto.setImageBitmap(bitmap)

            // Asignar datos a los TextViews
            textViewProducto.text = producto.nombre
            textViewSugerencia.text = "Sugerencia: ${producto.sugerencia}"
            textViewEstado.text = "Estado: ${producto.estado}"
        }
    }
}