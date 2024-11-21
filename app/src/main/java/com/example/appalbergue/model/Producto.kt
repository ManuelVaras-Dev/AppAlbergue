package com.example.appalbergue.model

data class Producto(
    val nombre: String = "",
    val sugerencia: String = "",
    val categoria: String = "",
    val subcategoria: String = "",
    val estado: String = "",
    var imagenBase64: String = ""
)