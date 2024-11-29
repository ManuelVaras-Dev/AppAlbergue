package com.example.appalbergue.model

import android.os.Parcel
import android.os.Parcelable

data class Rescate(
    val id: String = "",
    val nombre: String = "",
    val celular: String = "",
    val correo: String = "",
    var imagenBase64: String = "",
    val ubicacion: String = ""
) : Parcelable {

    // Constructor necesario para leer los datos del Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",       // id
        parcel.readString() ?: "",       // nombre
        parcel.readString() ?: "",       // celular
        parcel.readString() ?: "",       // correo
        parcel.readString() ?: "",       // imagenBase64
        parcel.readString() ?: ""        // ubicacion
    )

    // Método para escribir los datos del objeto en un Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(celular)
        parcel.writeString(correo)
        parcel.writeString(imagenBase64)
        parcel.writeString(ubicacion)
    }

    // Describe el contenido del objeto (generalmente se deja en 0)
    override fun describeContents(): Int = 0

    // El objeto Companion para crear el Parcelable
    companion object CREATOR : Parcelable.Creator<Rescate> {
        override fun createFromParcel(parcel: Parcel): Rescate {
            return Rescate(parcel)
        }

        override fun newArray(size: Int): Array<Rescate?> {
            return arrayOfNulls(size)
        }
    }
}


