package com.example.appalbergue.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.R
import com.example.appalbergue.model.Local
import java.io.ByteArrayInputStream


class LocalAdapter(
    private var localList: List<Local>,
    private val onEdit: (Local) -> Unit,
    private val onDelete: (Local) -> Unit
) : RecyclerView.Adapter<LocalAdapter.LocalViewHolder>() {

    fun updateList(newList: List<Local>) {
        localList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.local_btn_editar, parent, false)
        return LocalViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        val local = localList[position]
        holder.bind(local, onEdit, onDelete)
    }

    override fun getItemCount(): Int = localList.size

    class LocalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val localImageView: ImageView = itemView.findViewById(R.id.image_view_local)
        private val localNameTextView: TextView = itemView.findViewById(R.id.text_view_local)
        private val responsibleTextView: TextView = itemView.findViewById(R.id.text_view_responsable)
        private val phoneTextView: TextView = itemView.findViewById(R.id.text_view_telefono)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        fun bind(local: Local, onEdit: (Local) -> Unit, onDelete: (Local) -> Unit) {
            localNameTextView.text = local.local
            responsibleTextView.text = "Responsable: ${local.responsable}"
            phoneTextView.text = "Teléfono: ${local.telefono}"

            // Cargar imagen
            val bitmap = decodeBase64ToBitmap(local.imagenBase64)
            localImageView.setImageBitmap(bitmap)

            // Configurar acciones
            editButton.setOnClickListener { onEdit(local) }
            deleteButton.setOnClickListener { onDelete(local) }
        }

        private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                val inputStream = ByteArrayInputStream(decodedBytes)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}