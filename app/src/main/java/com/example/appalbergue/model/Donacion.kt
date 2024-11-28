package com.example.appalbergue.model

data class Donacion(
    val donanteNombre: String = "",
    val donanteCorreo: String = "",
    val donanteCelular: String = "",
    val donanteDireccion: String = "",
    val albergueNombre: String = "",
    val albergueEncargado: String = "",
    val albergueCelularEncargado: String = "",
    val albergueDireccionEncargado: String = "",
    val formaEnvio: String = ""
)
