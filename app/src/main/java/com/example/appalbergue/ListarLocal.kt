package com.example.appalbergue

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appalbergue.adapter.LocalAdapter
import com.example.appalbergue.model.Local
import com.google.firebase.database.*


class ListarLocal : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var addLocalButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var localAdapter: LocalAdapter
    private var localList: MutableList<Local> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_listar)

        recyclerView = findViewById(R.id.recycler_view)
        searchEditText = findViewById(R.id.edit_text_search_local)
        addLocalButton = findViewById(R.id.button_add_local)

        recyclerView.layoutManager = LinearLayoutManager(this)
        localAdapter = LocalAdapter(localList, ::editarLocal, ::eliminarLocal)
        recyclerView.adapter = localAdapter

        database = FirebaseDatabase.getInstance().getReference("locales")
        cargarLocales()

        addLocalButton.setOnClickListener {
            val intent = Intent(this, RegistroLocal::class.java)
            startActivity(intent)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarLocales(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun cargarLocales() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                localList.clear()
                for (localSnapshot in snapshot.children) {
                    val local = localSnapshot.getValue(Local::class.java)
                    local?.let { localList.add(it) }
                }
                localAdapter.updateList(localList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListarLocal, "Error al cargar locales", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarLocal(local: Local) {
        database.child(local.id).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Local eliminado con éxito", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al eliminar el local", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editarLocal(local: Local) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_local, null)
        val editName = dialogView.findViewById<EditText>(R.id.edit_name)
        val editResponsible = dialogView.findViewById<EditText>(R.id.edit_responsible)
        val editPhone = dialogView.findViewById<EditText>(R.id.edit_phone)

        editName.setText(local.local)
        editResponsible.setText(local.responsable)
        editPhone.setText(local.telefono)

        AlertDialog.Builder(this)
            .setTitle("Editar Local")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val updatedLocal = local.copy(
                    local = editName.text.toString(),
                    responsable = editResponsible.text.toString(),
                    telefono = editPhone.text.toString()
                )
                database.child(local.id).setValue(updatedLocal)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Local actualizado con éxito", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar el local", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun filtrarLocales(query: String) {
        val filteredList = localList.filter { it.local.contains(query, ignoreCase = true) }
        localAdapter.updateList(filteredList)
    }
}
