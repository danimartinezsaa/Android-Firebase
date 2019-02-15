package com.dani.notificaciones

import java.util.*

data class Datos(var token: String, var nombreDispositivo: String, var hora: Date){

    //Lista para almacenar los Datos
    val miHashMapDatos = HashMap<String, Any>()

    //Rellenamos el HashMap
    fun crearHashMapDatos() {
        miHashMapDatos.put("token", token)
        miHashMapDatos.put("nombre", nombreDispositivo)
        miHashMapDatos.put("hora", hora)
    }
}