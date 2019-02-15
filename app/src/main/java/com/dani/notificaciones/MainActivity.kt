package com.dani.notificaciones

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //Objeto para referenciar Database
    private var database: DatabaseReference? = null
    // Token del dispositivo
    private var token: String? = null
    // key unica creada automaticamente al añadir un child
    lateinit var misDatos : Datos
    lateinit var key: String
    // para actualizar los datos necesito un hash map
    val miHashMapChild = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Tiempo actualizado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            // cada vez que le damos click actualizamos tiempo
            misDatos.hora = Date()
            // Creamos el hashMap en el objeto
            misDatos.crearHashMapDatos()
            // actualizamos la base de datosw
            miHashMapChild.put(key.toString(),misDatos.miHashMapDatos)
            // actualizamos el child
            database!!.updateChildren(miHashMapChild)
        }

        //Para que no se ejecute al girar
        if (savedInstanceState == null) {
            try {
                //Obtengo el token del dispositivo.
                token = FirebaseInstanceId.getInstance().token
                //creamos una entrada nueva en el child "dispositivos" con un key unico automatico
                key = database!!.push().key!!
                //guardamos el token, dispositivo, tiempo actual en la data class
                misDatos = Datos(token.toString(),android.os.Build.MANUFACTURER+" "+android.os.Build.ID,Date())
                //creamos el hash map
                misDatos.crearHashMapDatos()
                //guardamos los datos en el hash map para la key creada anteriormente
                miHashMapChild.put(key.toString(), misDatos.miHashMapDatos)
                //actualizamos el child
                database!!.updateChildren(miHashMapChild)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Notificaciones","Error escribiendo datos ${e}")
            }
        }

        initListener()

    }

    /**
     * Listener que se activa al eliminar, cambiar, mover o añadir datos
     */
    fun initListener() {
        val childEventListener = object : ChildEventListener {
            override fun onChildRemoved(p0: DataSnapshot) {
                Log.d("Notificaciones", "Datos borrados: " + p0.key)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d("Notificaciones", "Datos cambiados: " + (p0.getValue() as HashMap<*,*>).toString())
                val has=(p0.child("hora").getValue() as HashMap<*,*>)
                txthora.text=has["hours"].toString()+" : "+has["minutes"].toString()+" : "+has["seconds"].toString()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d("Notificaciones", "Datos movidos")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // onChildAdded() capturamos la key
                Log.d("Notificaciones", "Datos añadidos: " + p0.key)

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("Notificaciones", "Error cancelacion")
            }
        }
        //Añadimos el listener a la base
        database!!.addChildEventListener(childEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
 }