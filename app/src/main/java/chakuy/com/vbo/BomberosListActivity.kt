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

class BomberosListActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    // Dos listas: una para mostrar y otra para guardar todos los datos
    private lateinit var listaCompleta: ArrayList<BomberoUnit>
    private lateinit var listaFiltrada: ArrayList<BomberoUnit>

    private lateinit var adapter: BomberoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bomberos_list)


        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE // Asegurar que se vea al iniciar

        recyclerView = findViewById(R.id.recyclerBomberos)
        // CAMBIO 1: Grilla de 4 Columnas
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)

        listaCompleta = arrayListOf()
        listaFiltrada = arrayListOf()

        // Inicializamos el adapter vacío
        adapter = BomberoAdapter(listaFiltrada)
        recyclerView.adapter = adapter

        getBomberosData()
        setupBuscador() // Configurar el buscador
        setupBottomNavigation()
    }

    private fun getBomberosData() {
        dbRef = FirebaseDatabase.getInstance().getReference("epr")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCompleta.clear()
                listaFiltrada.clear()

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val unit = userSnapshot.getValue(BomberoUnit::class.java)
                        if (unit != null) {
                            listaCompleta.add(unit)
                        }
                    }
                    // Al principio mostramos todos
                    listaFiltrada.addAll(listaCompleta)
                    adapter.notifyDataSetChanged()
                }
// 3. OCULTAR EL SPINNER CUANDO TERMINA LA CARGA
                progressBar.visibility = View.GONE

            }

            override fun onCancelled(error: DatabaseError) {
                // 4. OCULTAR EL SPINNER TAMBIÉN SI HAY ERROR
                progressBar.visibility = View.GONE
                Toast.makeText(this@BomberosListActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // --- CAMBIO 2: LÓGICA DEL BUSCADOR ---
    private fun setupBuscador() {
        // Asegúrate de ponerle este ID a tu EditText en el XML: android:id="@+id/etBuscador"
        // Si usaste otro ID en el XML anterior, búscalo por ese ID.
        // En el XML que te pasé antes, estaba dentro de un CardView pero no tenía ID propio.
        // Voy a asumir que lo encuentras dentro del CardView o le pones ID.

        // Opción A: Si le pusiste ID al EditText en el XML
        val etBuscador = findViewById<EditText>(R.id.etBuscador)

        // Opción B: Si no le pusiste ID y está dentro del CardView 'searchCard'
        // val searchCard = findViewById<androidx.cardview.widget.CardView>(R.id.searchCard)
        // val etBuscador = searchCard.getChildAt(0) as EditText

        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filtrar(s.toString())
            }
        })
    }

    private fun filtrar(texto: String) {
        val textoBusqueda = texto.lowercase()
        val listaTemporal = ArrayList<BomberoUnit>()

        for (item in listaCompleta) {
            val nombre = item.nombre?.lowercase() ?: ""
            val ciudad = item.ciudad?.lowercase() ?: ""

            // Buscamos por nombre O por ciudad
            if (nombre.contains(textoBusqueda) || ciudad.contains(textoBusqueda)) {
                listaTemporal.add(item)
            }
        }

        // Actualizamos el adaptador con la lista filtrada
        adapter.actualizarLista(listaTemporal)
    }

    // --- Navegación (Igual que antes) ---
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
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number&text=${Uri.encode(message)}")
            startActivity(intent)
        } catch (e: Exception) { }
    }
}