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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory // Importante para iconos personalizados
import com.google.android.material.floatingactionbutton.FloatingActionButton // Importante para el FAB

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    // Coordenada central (Ej: Cochabamba Centro)
    private val defaultLocation = LatLng(-17.3935, -66.1570)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // 1. Inicializar el Mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_full_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 2. Configurar la barra inferior
        setupBottomNavigation()

// NUEVO: Añadir un Floating Action Button para la leyenda
        val fabLeyenda: FloatingActionButton = findViewById(R.id.fab_leyenda)
        fabLeyenda.setOnClickListener {
            showLegendDialog()

        }

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Mover la cámara a la ubicación por defecto
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14f))
        googleMap.uiSettings.isZoomControlsEnabled = true



        // Añadir marcadores de ejemplo (Hospitales, Estaciones, etc.)
        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Centro de la Ciudad"))

        // Ejemplo: Puedes añadir más marcadores aquí manualmente
        googleMap.addMarker(MarkerOptions().position(LatLng(-17.37, -66.15)).title("Hospital Viedma").snippet("Servicios de Emergencia 24h").icon(getSmallIcon(R.drawable.hospital)))
        googleMap.addMarker(MarkerOptions().position(LatLng(-17.3807, -66.1594)).title("Bomberos Voluntarios Sar Bolivia").snippet("Servicios de Emergencia 24h").icon(getSmallIcon(R.drawable.bomberosvoluntarios)))
        googleMap.addMarker(MarkerOptions().position(LatLng(-17.3691, -66.1309)).title("Bomberos Voluntarios Yunka Atoq").snippet("Servicios de Emergencia 24h").icon(getSmallIcon(R.drawable.bomberosvoluntarios)))

        // Habilitar controles de zoom
        googleMap.uiSettings.isZoomControlsEnabled = true
    }
        // NUEVO: Función para mostrar el diálogo de leyenda
        private fun showLegendDialog() {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_legend, null)
            builder.setView(dialogView)
            builder.setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
    }

    // --- LÓGICA DE NAVEGACIÓN (Copiada para que funcione el menú) ---
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

            // Resaltar el icono si estamos en MAPA (Opcional visual)
            if (item.actionId == "MAP") {
                textView.setTextColor(getColor(R.color.primary_blue))
                iconView.setColorFilter(getColor(R.color.primary_blue))
            }

            navItemView.setOnClickListener { handleNavigationClick(item.actionId) }
            container.addView(navItemView)
        }
    }


    // --- FUNCIÓN PARA REDIMENSIONAR ICONOS ---
    private fun getSmallIcon(drawableId: Int): com.google.android.gms.maps.model.BitmapDescriptor {
        val height = 100 // Altura deseada en pixeles (ajusta este valor si quieres más grande/pequeño)
        val width = 100  // Ancho deseado
        val bitmap = android.graphics.BitmapFactory.decodeResource(resources, drawableId)
        val smallMarker = android.graphics.Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(smallMarker)
    }

    private fun handleNavigationClick(action: String) {
        when (action) {
            "HOME" -> {
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            "MAP" -> {
                Toast.makeText(this, "Ya estás viendo el mapa.", Toast.LENGTH_SHORT).show()
            }
            "REPORT" -> {
                startActivity(Intent(this, ReportEmergencyActivity::class.java))
            }
            "WHATSAPP_DEV" -> openWhatsappContact("+59170776212", "Hola...")
            "LOGIN" -> Toast.makeText(this, "Login...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWhatsappContact(number: String, message: String) {
        try {
            val numberCleaned = number.replace("+", "").replace(" ", "")
            val encodedMessage = Uri.encode(message)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$numberCleaned&text=$encodedMessage")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}