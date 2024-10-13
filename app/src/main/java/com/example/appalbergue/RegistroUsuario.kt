package com.example.appalbergue

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar

class RegistroUsuarioFragment : Fragment() {

    private lateinit var registerTitle: TextView
    private lateinit var profilePhoto: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var btnFechaNacimiento: Button
    private lateinit var btnRegistrar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.registro_usuario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar los elementos de la interfaz
        registerTitle = view.findViewById(R.id.registerTitle)
        profilePhoto = view.findViewById(R.id.profilePhoto)
        etNombre = view.findViewById(R.id.etNombre)
        etCorreo = view.findViewById(R.id.etCorreo)
        etContrasena = view.findViewById(R.id.etContrasena)
        etConfirmarContrasena = view.findViewById(R.id.etConfirmarContrasena)
        etTelefono = view.findViewById(R.id.etTelefono)
        etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento)
        btnFechaNacimiento = view.findViewById(R.id.btnFechaNacimiento)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)

        // Configuración del botón de seleccionar fecha de nacimiento
        btnFechaNacimiento.setOnClickListener {
            mostrarDatePicker()
        }

        // Lógica para el botón de registrar
        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val anio = calendario[Calendar.YEAR]
        val mes = calendario[Calendar.MONTH]
        val dia = calendario[Calendar.DAY_OF_MONTH]

        // Mostrar el DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val fechaSeleccionada = "$dayOfMonth/${monthOfYear + 1}/$year"
                etFechaNacimiento.setText(fechaSeleccionada)
            },
            anio, mes, dia
        )
        datePickerDialog.show()
    }

    private fun registrarUsuario() {
        val nombre = etNombre.text.toString()
        val correo = etCorreo.text.toString()
        val contrasena = etContrasena.text.toString()
        val confirmarContrasena = etConfirmarContrasena.text.toString()
        val telefono = etTelefono.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()

        // Validar campos
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Comprobar si las contraseñas coinciden
        if (contrasena != confirmarContrasena) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí puedes continuar con el registro (guardar en base de datos, etc.)
        Toast.makeText(requireContext(), "Registro de usuario exitoso", Toast.LENGTH_SHORT).show()
        // Agregar lógica para continuar el flujo después del registro
    }
}
