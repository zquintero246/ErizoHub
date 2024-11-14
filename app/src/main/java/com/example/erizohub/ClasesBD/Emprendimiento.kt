package com.example.erizohub.ClasesBD

class Emprendimiento(
    var idEmprendimiento: String,
    var nombre_emprendimiento: String,
    var descripcion: String,
    var imagenEmprendimiento: String,
    var listaProductos: List<Any> = mutableListOf(),
    var comentarios: List<Any> = mutableListOf()
)