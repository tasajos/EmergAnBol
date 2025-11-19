package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AmbulanciasListActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var listaCompleta: ArrayList<AmbulanciaUnit>
    private lateinit var listaFiltrada: ArrayList<AmbulanciaUnit>
    private lateinit var adapter: AmbulanciaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulancias_list)

        progressBar = findViewById(R.id.progressBar)

        recyclerView = findViewById(R.id.recyclerAmbulancias)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columnas
        recyclerView.setHasFixedSize(true)

        listaCompleta = arrayListOf()
        listaFiltrada = arrayListOf()

        adapter = AmbulanciaAdapter(listaFiltrada)
        recyclerView.adapter = adapter

        getAmbulanciasData()
        setupBuscador()
        setupBottomNavigation()
    }

    private fun getAmbulanciasData() {
        progressBar.visibility = View.VISIBLE
        // IMPORTANTE: La ruta exacta de tu carpeta en Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("ambulancia")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCompleta.clear()
                listaFiltrada.clear()

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val unit = userSnapshot.getValue(AmbulanciaUnit::class.java)
                        if (unit != null) {
                            listaCompleta.add(unit)
                        }
                    }
                    listaFiltrada.addAll(listaCompleta)
                    adapter.notifyDataSetChanged()
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AmbulanciasListActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupBuscador() {
        val etBuscador = findViewById<EditText>(R.id.etBuscador)
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrar(s.toString())
            }
        })
    }

    private fun filtrar(texto: String) {
        val busqueda = texto.lowercase()
        val temporal = ArrayList<AmbulanciaUnit>()
        for (item in listaCompleta) {
            val nombre = item.nombre?.lowercase() ?: ""
            val ciudad = item.ciudad?.lowercase() ?: ""
            if (nombre.contains(busqueda) || ciudad.contains(busqueda)) {
                temporal.add(item)
            }
        }
        adapter.actualizarLista(temporal)
    }

    // --- Navegación estándar ---
    private fun setupBottomNavigation() {
        val container: LinearLayout = findViewById(R.id.bottom_nav_container)
        val inflater = LayoutInflater.from(this)
        val navItems = listOf(
            NavItem("Inicio", R.drawable.hogar, "HOME"),
            NavItem("Mapa", R.drawable.ubicaciones, "MAP"),
            NavItem("Emergencia", R.drawable.sirena, "REPORT"),
            NavItem("WhatsApp", R.drawable.whats, "WHATSAPP_DEV"),
            NavItem("Login", R.drawable.seguridad, "LOGIN")
        )
        for (item in navItems) {
            val navItemView = inflater.inflate(R.layout.item_bottom_nav, container, false) as LinearLayout
            val iconView: ImageView = navItemView.findViewById(R.id.nav_icon)
            val textView: TextView = navItemView.findViewById(R.id.nav_text)
            iconView.setImageResource(item.iconResId)
            textView.text = item.title
            navItemView.setOnClickListener { handleNavigationClick(item.actionId) }
            container.addView(navItemView)
        }
    }

    private fun handleNavigationClick(action: String) {
        when (action) {
            "HOME" -> { startActivity(Intent(this, MainMenuActivity::class.java)); finish() }
            "MAP" -> startActivity(Intent(this, MapsActivity::class.java))
            "REPORT" -> startActivity(Intent(this, ReportEmergencyActivity::class.java))
            "WHATSAPP_DEV" -> openWhatsappContact("+59170776212", "Hola...")
            "LOGIN" -> Toast.makeText(this, "Login...", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openWhatsappContact(number: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$number"))
            startActivity(intent)
        } catch (e: Exception) { }
    }
}