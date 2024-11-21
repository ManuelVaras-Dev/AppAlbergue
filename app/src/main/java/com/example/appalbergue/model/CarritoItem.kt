package com.example.appalbergue.model

data class CarritoItem(
    val id: String = "", // Unique key for each cart item
    val nombre: String = "",
    val comentarios: String = "",
    val imagen: String = "",
    val cantidad: Int = 1
)
