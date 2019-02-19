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
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity() {

    //Objeto para referenciar Database
    private var database: DatabaseReference? = null
    lateinit var misDatos : Datos
    lateinit var usuario: String
    // para actualizar los datos necesito un hash map
    val miHashMapChild = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // referencia a la base de datos del proyecto en firebase
        database = FirebaseDatabase.getInstance().getReference("/dispositivos")

        fab.setOnClickListener { view ->
            if(innombre.text!=null){
                Snackbar.make(view, "Pedido enviado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                misDatos = Datos("","","","","",false)

                misDatos.cafeleche = incafeleche.text.toString()
                misDatos.cafesololargo = incafesololargo.text.toString()
                misDatos.croissant = incroissant.text.toString()
                misDatos.tostada = intostada.text.toString()
                misDatos.tortilla = intortilla.text.toString()
                misDatos.confirmado = false

                // Creamos el hashMap en el objeto
                misDatos.crearHashMapDatos()
                usuario=innombre.text.toString()
                // actualizamos la base de datos
                miHashMapChild.put(usuario,misDatos.miHashMapDatos)
                // actualizamos el child
                database!!.updateChildren(miHashMapChild)
            }else{
                toast("Inserte Nombre para el pedido")
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
                Log.d("Bar", "Datos borrados")
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d("Bar", "Datos cambiados: " + (p0.getValue() as HashMap<*,*>).toString())
                val lista=(p0.getValue() as HashMap<*,*>)
                val confirmado=lista["confirmado"].toString()
                if(confirmado.equals("true")){
                    checkConfirmado.setChecked(true)
                }else{
                    checkConfirmado.setChecked(false)
                }
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d("Notificaciones", "Datos movidos")
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // onChildAdded() capturamos la key
                Log.d("Notificaciones", "Datos añadidos.")
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