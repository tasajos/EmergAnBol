package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.view.LayoutInflater

class AmbulanciaDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var currentUnit: AmbulanciaUnit
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulancia_detail)

        // 1. Recibir el Objeto Serializable
        currentUnit = intent.getSerializableExtra("UNIT_DATA") as? AmbulanciaUnit ?: AmbulanciaUnit()

        // 2. Cargar Datos en la UI
        setupUI()
        setupButtons()
        setupBottomNavigation()

        // 3. Iniciar Mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_unit_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupUI() {
        val tvName = findViewById<TextView>(R.id.tvUnitName)
        val tvCity = findViewById<TextView>(R.id.tvUnitCity)
        val imgLogo = findViewById<ImageView>(R.id.imgDetailLogo)

        tvName.text = currentUnit.nombre ?: "Unidad Desconocida"
        tvCity.text = currentUnit.ciudad ?: "Bolivia"

        if (!currentUnit.imagen.isNullOrEmpty()) {
            Glide.with(this).load(currentUnit.imagen).into(imgLogo)
        }
    }

    private fun setupButtons() {
        // LLAMAR
        findViewById<LinearLayout>(R.id.btnActionCall).setOnClickListener {
            val phone = currentUnit.getTelefonoString()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Teléfono no disponible", Toast.LENGTH_SHORT).show()
            }
        }

        // WHATSAPP
        findViewById<LinearLayout>(R.id.btnActionWhatsapp).setOnClickListener {
            val wapp = currentUnit.getWhatsappString()
            if (wapp.isNotEmpty()) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$wapp")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "WhatsApp no disponible", Toast.LENGTH_SHORT).show()
            }
        }

        // FACEBOOK
        // Usamos R.id.btnActionFacebook (el ID del layout), NO R.drawable
        findViewById<LinearLayout>(R.id.btnActionFacebook).setOnClickListener {
            openUrl(currentUnit.facebook)
        }

        // WEB
        // Usamos R.id.btnActionWeb (el ID del layout)
        findViewById<LinearLayout>(R.id.btnActionWeb).setOnClickListener {
            openUrl(currentUnit.web)
        }
    }

    private fun openUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Enlace no válido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Enlace no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Validar si tenemos coordenadas
        val lat = currentUnit.latitude
        val lon = currentUnit.longitude

        if (lat != null && lon != null && lat != 0.0) {
            val location = LatLng(lat, lon)
            googleMap.addMarker(MarkerOptions().position(location).title(currentUnit.nombre))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            googleMap.uiSettings.isZoomControlsEnabled = true
        } else {
            Toast.makeText(this, "Esta unidad no tiene ubicación registrada", Toast.LENGTH_SHORT).show()
        }
    }

    // --- BARRA INFERIOR (Standard) ---
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
            "WHATSAPP_DEV" -> { /* Lógica existente */ }
            "LOGIN" -> Toast.makeText(this, "Login...", Toast.LENGTH_SHORT).show()
        }
    }
}