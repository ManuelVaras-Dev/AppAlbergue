package com.example.appalbergue.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.util.Base64
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.MapActivity
import com.example.appalbergue.R
import com.example.appalbergue.model.Rescate

class RescateAdapter(private val rescateList: List<Rescate>, private val onClick: (Rescate) -> Unit) :
    RecyclerView.Adapter<RescateAdapter.PersonaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rescate, parent, false)
        return PersonaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        val persona = rescateList[position]
        holder.bind(persona, onClick)
    }

    override fun getItemCount(): Int = rescateList.size

    class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewProfile: ImageView = itemView.findViewById(R.id.imageViewProfile)
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        private val txtCelular: TextView = itemView.findViewById(R.id.txtCelular)
        private val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreo)
        private val txtUbicacion: TextView = itemView.findViewById(R.id.txtUbicacion) // NUEVO
        private val mapPreview: ImageView = itemView.findViewById(R.id.mapPreview)
        private val btnExpandMap: ImageButton = itemView.findViewById(R.id.btnExpandMap)
        private val mapPreviewContainer: FrameLayout = itemView.findViewById(R.id.mapPreviewContainer)

        fun bind(rescate: Rescate, onClick: (Rescate) -> Unit) {
            // Asignar los datos de texto
            txtNombre.text = rescate.nombre
            txtCelular.text = rescate.celular
            txtCorreo.text = rescate.correo
            txtUbicacion.text = rescate.ubicacion

            // Si la imagen está en Base64, convertirla a Bitmap
            if (rescate.imagenBase64.isNotEmpty()) {
                val decodedString: ByteArray = Base64.decode(rescate.imagenBase64, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageViewProfile.setImageBitmap(decodedBitmap)
            } else {
                // Si no hay imagen, usa un recurso predeterminado (placeholder)
                imageViewProfile.setImageResource(R.drawable.place_holder_imagen)
            }

            // Configurar la vista previa del mapa
            mapPreview.setImageResource(R.drawable.map_placeholder)  // Imagen de mapa estático (puedes sustituirla por otro mapa)

            // Acción al hacer clic en la vista previa del mapa
            mapPreviewContainer.setOnClickListener {
                // Aquí podrías abrir una actividad o un fragmento con el mapa completo
                onClick(rescate)  // Pasar el objeto Persona para obtener detalles
            }

            // Acción para expandir el mapa cuando el botón sea presionado
            btnExpandMap.setOnClickListener {
                // Llamar a una función para expandir el mapa (puedes usar un Fragment para mostrar un mapa completo)
                expandMap(rescate)
            }
        }

        private fun expandMap(rescate: Rescate) {
            // Aquí es donde mostrarías un mapa completo en una nueva actividad o fragmento
            // Esta es solo una acción básica, podrías hacer lo que desees
            Toast.makeText(itemView.context, "Mostrando ubicación", Toast.LENGTH_SHORT).show()

            // Aquí podrías abrir una nueva actividad o fragmento con el mapa completo
            val intent = Intent(itemView.context, MapActivity::class.java)
            intent.putExtra("persona", rescate)  // Pasar los datos de la persona al map completo
            itemView.context.startActivity(intent)
        }
    }
}
