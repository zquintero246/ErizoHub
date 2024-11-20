package com.example.erizohub.ClasesBD

data class Producto(
    var id_producto: String = "",
    var nombre_producto: String = "",
    var descripcionProducto: String = "",
    var imagen_producto: String = "",
    var idEmprendimiento: String = "",
    var comentarios: List<Comentario> = emptyList(),
    var likes: Int = 0
)
