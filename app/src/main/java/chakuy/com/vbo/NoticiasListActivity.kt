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

class NoticiasListActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var listaCompleta: ArrayList<Noticia>
    private lateinit var adapter: NoticiasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias_list) // Layout para Noticias

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        recyclerView = findViewById(R.id.recyclerNoticias) // ID para RecyclerView de noticias
        recyclerView.layoutManager = LinearLayoutManager(this) // Lista vertical de posts
        recyclerView.setHasFixedSize(true)

        listaCompleta = arrayListOf()
        adapter = NoticiasAdapter(listaCompleta)
        recyclerView.adapter = adapter

        getNoticiasData()
        setupBottomNavigation()
    }

    private fun getNoticiasData() {
        // REFERENCIA A FIREBASE: /noticias
        dbRef = FirebaseDatabase.getInstance().getReference("noticias")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCompleta.clear()
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val item = ds.getValue(Noticia::class.java)
                        if (item != null) listaCompleta.add(item)
                    }
                    adapter.actualizarLista(listaCompleta)
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@NoticiasListActivity, "Error al cargar noticias.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // --- COPIA Y PEGA LAS FUNCIONES DE NAVEGACIÓN ESTÁNDAR AQUÍ ---
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