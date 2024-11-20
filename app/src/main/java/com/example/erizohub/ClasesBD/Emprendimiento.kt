package com.example.erizohub.ClasesBD


data class Emprendimiento(
    val idEmprendimiento: String = "",
    val nombre_emprendimiento: String = "",
    val descripcion: String = "",
    val imagenEmprendimiento: String = "",
    val listaProductos: List<Producto> = emptyList(),
    val comentarios: List<Comentario> = emptyList(),
    val likes: Int = 0
)
