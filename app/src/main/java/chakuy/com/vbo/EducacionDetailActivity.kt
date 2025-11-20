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
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EducacionDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var currentUnit: EducacionUnit // <--- TIPO CORRECTO
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educacion_detail) // <--- LAYOUT CORRECTO

        // Recibir datos
        currentUnit = intent.getSerializableExtra("UNIT_DATA") as? EducacionUnit ?: EducacionUnit()

        setupUI()
        setupButtons()
        setupBottomNavigation()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_unit_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupUI() {
        val tvName = findViewById<TextView>(R.id.tvUnitName)
        val tvCity = findViewById<TextView>(R.id.tvUnitCity)
        val imgLogo = findViewById<ImageView>(R.id.imgDetailLogo)

        tvName.text = currentUnit.nombre ?: "Desconocido"
        tvCity.text = currentUnit.ciudad ?: "Bolivia"
        if (!currentUnit.imagen.isNullOrEmpty()) {
            Glide.with(this).load(currentUnit.imagen).into(imgLogo)
        }
    }

    private fun setupButtons() {
        findViewById<LinearLayout>(R.id.btnActionCall).setOnClickListener {
            val ph = currentUnit.getTelefonoString()
            if(ph.isNotEmpty()) startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$ph")))
        }
        findViewById<LinearLayout>(R.id.btnActionWhatsapp).setOnClickListener {
            val wp = currentUnit.getWhatsappString()
            if(wp.isNotEmpty()) openUrl("https://api.whatsapp.com/send?phone=$wp")
        }
        findViewById<LinearLayout>(R.id.btnActionFacebook).setOnClickListener { openUrl(currentUnit.facebook) }
        findViewById<LinearLayout>(R.id.btnActionWeb).setOnClickListener { openUrl(currentUnit.web) }
    }

    private fun openUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) } catch (e: Exception) {}
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val lat = currentUnit.latitude
        val lon = currentUnit.longitude
        if (lat != null && lon != null && lat != 0.0) {
            val pos = LatLng(lat, lon)
            googleMap.addMarker(MarkerOptions().position(pos).title(currentUnit.nombre))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
        }
    }

    // --- COPIA LAS FUNCIONES DE NAVEGACIÓN (setupBottomNavigation) AQUÍ ---
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