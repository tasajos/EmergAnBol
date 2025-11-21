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

class EducacionListActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var listaCompleta: ArrayList<EducacionUnit>
    private lateinit var adapter: EducacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educacion_list) // <--- Layout correcto

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        recyclerView = findViewById(R.id.recyclerAmbulancias) // O el ID que tengas
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        listaCompleta = arrayListOf()
        adapter = EducacionAdapter(arrayListOf())
        recyclerView.adapter = adapter

        getData()
        setupBuscador()
        setupBottomNavigation()
    }

    private fun getData() {
        // REFERENCIA A FIREBASE: /educacion
        dbRef = FirebaseDatabase.getInstance().getReference("educacion")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCompleta.clear()
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val unit = ds.getValue(EducacionUnit::class.java)
                        if (unit != null) listaCompleta.add(unit)
                    }
                    adapter.actualizarLista(listaCompleta)
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun setupBuscador() {
        val etBuscador = findViewById<EditText>(R.id.etBuscador)
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().lowercase()
                val filtrada = ArrayList<EducacionUnit>()
                for (item in listaCompleta) {
                    if ((item.nombre?.lowercase()?.contains(texto) == true) ||
                        (item.ciudad?.lowercase()?.contains(texto) == true)) {
                        filtrada.add(item)
                    }
                }
                adapter.actualizarLista(filtrada)
            }
        })
    }

    // --- COPIA Y PEGA LAS FUNCIONES DE NAVEGACIÓN (setupBottomNavigation, etc.) AQUÍ ---
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