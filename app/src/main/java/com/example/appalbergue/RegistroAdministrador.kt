package com.example.appalbergue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment


class RegistroAdministradorFragment : Fragment() {

    private lateinit var etNameOrCompany: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNickname: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegisterAdmin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.registro_administrador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enlazando los EditText y el botón con los elementos del layout
        etNameOrCompany = view.findViewById(R.id.et_name_or_company)
        etEmail = view.findViewById(R.id.et_personal_or_business_email)
        etNickname = view.findViewById(R.id.et_nickname)
        etPhone = view.findViewById(R.id.et_phone)
        etAddress = view.findViewById(R.id.et_address)
        etPassword = view.findViewById(R.id.et_password)
        etConfirmPassword = view.findViewById(R.id.et_confirm_password)
        btnRegisterAdmin = view.findViewById(R.id.btn_register_admin)

        // Agregar un listener al botón de registro
        btnRegisterAdmin.setOnClickListener {
            val nameOrCompany = etNameOrCompany.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val nickname = etNickname.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Verificar que todos los campos estén llenos
            if (nameOrCompany.isEmpty() || email.isEmpty() || nickname.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar que las contraseñas coincidan
            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí puedes agregar la lógica para guardar los datos en tu base de datos o hacer una llamada a un servidor.
            Toast.makeText(requireContext(), "Registro de administrador exitoso", Toast.LENGTH_SHORT).show()

            // Después del registro exitoso, puedes finalizar el fragmento o navegar a otra pantalla.
            // En este caso, podrías navegar a la verificación o a la pantalla principal.
        }
    }
}
