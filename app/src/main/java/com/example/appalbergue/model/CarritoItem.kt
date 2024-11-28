package com.example.appalbergue.model

data class CarritoItem(
    var id: String = "", // Unique key for each cart item
    val nombre: String = "",
    val comentarios: String = "",
    val imagen: String = "",
    var cantidad: Int = 0
)

