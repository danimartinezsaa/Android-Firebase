package com.dani.notificaciones

import java.util.*

data class Datos(var cafeleche: String, var cafesololargo: String, var croissant: String, var tostada: String, var tortilla: String,var confirmado: Boolean){

    //Lista para almacenar los Datos
    val miHashMapDatos = HashMap<String, Any>()

    //Rellenamos el HashMap
    fun crearHashMapDatos() {
        miHashMapDatos.put("cafeleche", cafeleche)
        miHashMapDatos.put("cafesololargo", cafesololargo)
        miHashMapDatos.put("croissant", croissant)
        miHashMapDatos.put("tostada", tostada)
        miHashMapDatos.put("tortilla", tortilla)
        miHashMapDatos.put("confirmado",confirmado)

    }
}