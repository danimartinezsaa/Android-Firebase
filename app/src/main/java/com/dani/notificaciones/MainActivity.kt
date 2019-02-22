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
    //Token del dispositivo
    private var FCMToken: String? = null
    //Para actualizar los datos necesito un hash map
    val miHashMapChild = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Obtengo el token del dispositivo.
        FCMToken = FirebaseInstanceId.getInstance().token
        //referencia a la base de datos del proyecto en firebase
        database = FirebaseDatabase.getInstance().getReference("/")

        //Para recoger los datos si abrimos la App desde la notificación, mostramos los datos que nos da
        if (getIntent().getExtras() != null) {
            Log.d("Notificacion","Recibiste confirmado:"+getIntent().getExtras().getString("confirmado"))
            innombre.setText(getIntent().getExtras().getString("usuario"))
            incafeleche.setText(getIntent().getExtras().getString("cafeleche"))
            incafesololargo.setText(getIntent().getExtras().getString("cafesololargo"))
            incroissant.setText(getIntent().getExtras().getString("croissant"))
            intortilla.setText(getIntent().getExtras().getString("tortilla"))
            intostada.setText(getIntent().getExtras().getString("tostada"))
            if(getIntent().getExtras().getString("confirmado")!=null){
                if(intent.extras.getString("confirmado").equals("true"))
                    checkConfirmado.setChecked(true)
            }

        }

        //Listener del botón para enviar pedido
        fab.setOnClickListener { view ->
            if(innombre.text!=null){
                Snackbar.make(view, "Pedido enviado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                misDatos = Datos("","","","","","",false)

                misDatos.usuario=innombre.text.toString()
                misDatos.cafeleche = incafeleche.text.toString()
                misDatos.cafesololargo = incafesololargo.text.toString()
                misDatos.croissant = incroissant.text.toString()
                misDatos.tostada = intostada.text.toString()
                misDatos.tortilla = intortilla.text.toString()
                misDatos.confirmado = false

                //Creamos el hashMap en el objeto
                misDatos.crearHashMapDatos()

                //Añadimos el hashMap a otro con el token del dispositivo, para identificarlo en la base de datos
                miHashMapChild.put(FCMToken.toString(),misDatos.miHashMapDatos)
                //Actualizamos el child de la base
                database!!.updateChildren(miHashMapChild)
            }else{
                toast("Inserte Nombre para el pedido")
            }

        }

        //Listener para actualizar los datos en tiempo real con la base de datos
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