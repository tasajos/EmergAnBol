package chakuy.com.vbo

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// --- IMPORTS DE GOOGLE MAPS Y UBICACIÓN ---
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class ReportEmergencyActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    // --- VARIABLES DE MAPS FALTANTES ---
    private lateinit var googleMap: GoogleMap
    private var incidentMarker: Marker? = null
    private val cochabamba = LatLng(-17.4000, -66.1500) // Coordenada de Cochabamba

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_emergency)

        // Inicializa la fecha y hora
        initDateTime()

        // Inicializa ubicacion
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializar el Mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configura la navegación inferior y lógica de clics
        setupBottomNavigation() // ¡Ahora esta función existe!

        // Botón: Obtener Ubicación
        findViewById<Button>(R.id.btnObtenerUbicacion).setOnClickListener {
            requestLocationPermission()
        }

        // Manejar el clic del botón de Reportar
        findViewById<Button>(R.id.btnReportar).setOnClickListener {
            // Lógica de envío de datos del formulario
            Toast.makeText(this, "Reporte Enviado. Gracias.", Toast.LENGTH_LONG).show()
            finish() // Cierra la activity y vuelve al MainMenu
        }
    }

    private fun updateMapPin(latLng: LatLng, title: String) {
        incidentMarker?.remove() // Remover el marcador anterior si existe
        incidentMarker = googleMap.addMarker(MarkerOptions()
            .position(latLng)
            .title(title)
            .draggable(true))

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Mover la cámara inicial a Cochabamba
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cochabamba, 12f))

        // 1. Manejo del Arrastre del Marcador
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {

            // --- FUNCIONES OBLIGATORIAS AÑADIDAS ---
            override fun onMarkerDragStart(marker: Marker) {
                // No es necesario código aquí, pero la función debe existir
            }

            override fun onMarkerDrag(marker: Marker) {
                // No es necesario código aquí, pero la función debe existir
            }
            // ----------------------------------------

            override fun onMarkerDragEnd(marker: Marker) {
                val newPosition = marker.position
                findViewById<TextView>(R.id.tvCoordenadas).text =
                    String.format("Lat: %.4f, Lon: %.4f (Manual)", newPosition.latitude, newPosition.longitude)
            }
        })

        // 2. Manejo del Clic en el Mapa (Registrar otra ubicación)
        googleMap.setOnMapClickListener { latLng ->
            updateMapPin(latLng, "Ubicación Registrada Manualmente")
            findViewById<TextView>(R.id.tvCoordenadas).text =
                String.format("Lat: %.4f, Lon: %.4f (Manual)", latLng.latitude, latLng.longitude)
        }
    }

    private fun initDateTime() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDate = Date()

        findViewById<TextView>(R.id.tvFecha).text = dateFormat.format(currentDate)
        findViewById<TextView>(R.id.tvHora).text = timeFormat.format(currentDate)
        findViewById<TextView>(R.id.tvEstado).text = "Pendiente"
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar el permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permiso ya concedido, obtener ubicación
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation() // Permiso concedido
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. No se puede obtener la ubicación actual.", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getLastLocation() {
        // Verificar si el permiso fue concedido justo antes de usar la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return // Salir si el permiso no está disponible (esto no debería ocurrir si requestLocationPermission funciona)
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)

                // 1. Actualizar el TextView de coordenadas
                findViewById<TextView>(R.id.tvCoordenadas).text =
                    String.format("Lat: %.4f, Lon: %.4f (GPS)", location.latitude, location.longitude)

                // 2. Mover y fijar el pin en el mapa
                updateMapPin(currentLatLng, "Ubicación Actual (GPS)")

            } else {
                Toast.makeText(this, "Ubicación no disponible. Intente de nuevo.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // =======================================================
    // --- LÓGICA COPIADA PARA LA BARRA DE NAVEGACIÓN INFERIOR ---
    // =======================================================

    private fun setupBottomNavigation() {
        val container: LinearLayout = findViewById(R.id.bottom_nav_container)
        val inflater = LayoutInflater.from(this)

        // Asegúrate de que estos drawables (PNG) existan en res/drawable/
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

            navItemView.setOnClickListener {
                handleNavigationClick(item.actionId)
            }

            container.addView(navItemView)
        }
    }

    private fun handleNavigationClick(action: String) {
        when (action) {
            "HOME" -> {
                // Volver a la MainMenuActivity al presionar HOME
                finish()
                Toast.makeText(this, "Volviendo a la página principal.", Toast.LENGTH_SHORT).show()
            }
            "MAP" -> {
                Toast.makeText(this, "Abriendo el mapa de ubicaciones.", Toast.LENGTH_SHORT).show()
                // Aquí iría el Intent para abrir MapasActivity
            }
            "REPORT" -> {
                // Ya estamos aquí, no hace falta hacer nada
                Toast.makeText(this, "Ya estás en la pantalla de Reporte.", Toast.LENGTH_SHORT).show()
            }
            "WHATSAPP_DEV" -> {
                openWhatsappContact("+59170776212", "Hola, quiero contactar al equipo de desarrollo acerca de la app...")
            }
            "LOGIN" -> {
                Toast.makeText(this, "Abriendo pantalla de inicio de sesión.", Toast.LENGTH_SHORT).show()
                // Aquí iría el Intent para abrir LoginActivity
            }
        }
    }

    // --- FUNCIONES AUXILIARES DE WHATSAPP/URL ---

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: No se pudo abrir la aplicación o el enlace.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWhatsappContact(number: String, message: String) {
        try {
            val numberCleaned = number.replace("+", "")
            val encodedMessage = Uri.encode(message)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$numberCleaned&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir WhatsApp.", Toast.LENGTH_SHORT).show()
        }
    }
}

// =======================================================
// --- CLASE DE DATOS NECESARIA (DEBE IR FUERA DE LA CLASE) ---
// =======================================================

//data class NavItem(val title: String, val iconResId: Int, val actionId: String)