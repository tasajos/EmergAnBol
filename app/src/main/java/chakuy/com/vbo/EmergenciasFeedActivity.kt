package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class EmergenciasFeedActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var listaEmergencias: ArrayList<EmergenciaReporte>
    private lateinit var adapter: EmergenciasFeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usaremos el mismo layout de noticias o uno nuevo,
        // para simplificar crearemos uno nuevo o reutilizamos si es idéntico.
        // Creemos uno nuevo para el título correcto.
        setContentView(R.layout.activity_emergencias_feed)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerFeed)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        listaEmergencias = arrayListOf()
        adapter = EmergenciasFeedAdapter(listaEmergencias)
        recyclerView.adapter = adapter

        getEmergenciasData()
        setupBottomNavigation()
    }

    private fun getEmergenciasData() {
        progressBar.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("ultimasEmergencias")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaEmergencias.clear()
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val reporte = ds.getValue(EmergenciaReporte::class.java)

                        // --- FILTRO: SOLO MOSTRAR 'EN PROCESO' ---
                        if (reporte != null) {
                            val estado = reporte.estado?.lowercase() ?: ""
                            // Aceptamos "en proceso" o variantes
                            if (estado.contains("proceso")) {
                                listaEmergencias.add(0, reporte) // Add(0) para mostrar las más recientes arriba
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        })
    }

    // --- NAVEGACIÓN ESTÁNDAR ---
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
            "WHATSAPP_DEV" -> { /* ... */ }
            "LOGIN" -> Toast.makeText(this, "Login...", Toast.LENGTH_SHORT).show()
        }
    }
}