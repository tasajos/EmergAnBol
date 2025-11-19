package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.* // Imports de Firebase

class BomberosListActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var bomberoArrayList: ArrayList<BomberoUnit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bomberos_list)

        // 1. Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerBomberos)
        // GridLayoutManager(context, numero_de_columnas) -> 2 columnas como en tu imagen
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        bomberoArrayList = arrayListOf()

        // 2. Obtener datos de Firebase
        getBomberosData()

        // 3. Configurar Barra Inferior
        setupBottomNavigation()
    }

    private fun getBomberosData() {
        // Referencia al nodo "epr" (o "unidades" según tu estructura exacta)
        // Asumo que la ruta es raiz -> epr -> [lista de unidades]
        dbRef = FirebaseDatabase.getInstance().getReference("epr")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bomberoArrayList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val unit = userSnapshot.getValue(BomberoUnit::class.java)
                        if (unit != null) {
                            bomberoArrayList.add(unit)
                        }
                    }
                    // Asignar adaptador
                    recyclerView.adapter = BomberoAdapter(bomberoArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BomberosListActivity, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // --- LÓGICA DE NAVEGACIÓN COMPARTIDA ---
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
            "HOME" -> {
                startActivity(Intent(this, MainMenuActivity::class.java))
                finish()
            }
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
        } catch (e: Exception) {
            Toast.makeText(this, "Error WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}