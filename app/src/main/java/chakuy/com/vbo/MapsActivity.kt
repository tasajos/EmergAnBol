package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button // Importante
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog // Importante
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.bumptech.glide.Glide // Importante para la imagen

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var dbRef: DatabaseReference
    private val defaultLocation = LatLng(-17.3935, -66.1570)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_full_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupBottomNavigation()

        findViewById<FloatingActionButton>(R.id.fab_leyenda).setOnClickListener {
            showLegendDialog()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 13f))
        googleMap.uiSettings.isZoomControlsEnabled = true

        // --- CARGA DE MARCADORES ---
        // Pasamos la CLASE específica para que Firebase sepa cómo convertirlo
        loadMarkersFromFirebase("epr", R.drawable.bomberosvoluntarios, BomberoUnit::class.java)
        loadMarkersFromFirebase("hospitales", R.drawable.hospital, HospitalUnit::class.java)
        loadMarkersFromFirebase("ambulancia", R.drawable.ambulancias, AmbulanciaUnit::class.java)
        loadMarkersFromFirebase("animalistas", R.drawable.animalistas, AnimalistaUnit::class.java)

        // --- LISTENER DE CLIC EN MARCADOR ---
        googleMap.setOnMarkerClickListener { marker ->
            // Recuperamos el objeto guardado en el Tag
            val unitData = marker.tag as? SearchableUnit
            if (unitData != null) {
                showBottomSheet(unitData) // Mostramos el detalle
            } else {
                // Es un marcador estático o sin datos (como el del centro)
                marker.showInfoWindow()
            }
            true // Devuelve true para indicar que consumimos el evento (no abrir infoWindow default)
        }
    }

    // --- FUNCIÓN GENÉRICA ACTUALIZADA ---
    // Ahora recibe el tipo de clase (clazz) para convertir los datos correctamente
    private fun <T : SearchableUnit> loadMarkersFromFirebase(path: String, iconResId: Int, clazz: Class<T>) {
        dbRef = FirebaseDatabase.getInstance().getReference(path)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (unitSnap in snapshot.children) {
                        // Convertimos al objeto específico (BomberoUnit, HospitalUnit, etc)
                        val unit = unitSnap.getValue(clazz)

                        if (unit != null && unit.latitude != null && unit.longitude != null) {
                            val location = LatLng(unit.latitude!!, unit.longitude!!)

                            val marker = googleMap.addMarker(
                                MarkerOptions()
                                    .position(location)
                                    .title(unit.nombre)
                                    .icon(getSmallIcon(iconResId))
                            )

                            // ¡TRUCO!: Guardamos todo el objeto dentro del marcador
                            marker?.tag = unit
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // --- MOSTRAR BOTTOM SHEET ---
    private fun showBottomSheet(unit: SearchableUnit) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_map_detail_sheet, null)
        dialog.setContentView(view)

        // Vincular vistas
        val tvTitle = view.findViewById<TextView>(R.id.sheetTitle)
        val tvCity = view.findViewById<TextView>(R.id.sheetCity)
        val tvPhone = view.findViewById<TextView>(R.id.sheetPhone)
        val imgView = view.findViewById<ImageView>(R.id.sheetImage)
        val btnCall = view.findViewById<Button>(R.id.btnSheetCall)
        val btnWapp = view.findViewById<Button>(R.id.btnSheetWhatsapp)

        // Llenar datos
        tvTitle.text = unit.nombre
        tvCity.text = unit.ciudad ?: "Bolivia"
        tvPhone.text = unit.getTelefonoString()

        // Cargar Imagen con Glide
        if (!unit.imagen.isNullOrEmpty()) {
            Glide.with(this).load(unit.imagen).circleCrop().into(imgView)
        } else {
            imgView.setImageResource(R.drawable.hogar) // Imagen por defecto si no hay
        }

        // Acción Llamar
        btnCall.setOnClickListener {
            val phone = unit.getTelefonoString()
            if (phone.isNotEmpty()) {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
            } else {
                Toast.makeText(this, "Sin teléfono", Toast.LENGTH_SHORT).show()
            }
        }

        // Acción WhatsApp (Intenta obtener whatsapp, si no usa telefono)
        btnWapp.setOnClickListener {
            // Como SearchableUnit no obliga a tener getWhatsappString en la interfaz (depende de tu DataModels),
            // intentamos castear o usar telefono como fallback.
            // Si añadiste getWhatsappString() a la interfaz SearchableUnit en el paso anterior, úsalo directo.
            // Si no, usaremos el teléfono:
            val wapp = unit.getTelefonoString()
            openWhatsappContact(wapp, "Hola, vi su ubicación en el mapa de Emergencias.")
        }

        dialog.show()
    }

    private fun getSmallIcon(drawableId: Int): BitmapDescriptor {
        val height = 100
        val width = 100
        val bitmap = android.graphics.BitmapFactory.decodeResource(resources, drawableId)
        val smallMarker = android.graphics.Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(smallMarker)
    }

    private fun showLegendDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_legend, null)
        builder.setView(view)
        builder.setPositiveButton("Cerrar") { d, _ -> d.dismiss() }
        builder.create().show()
    }

    // ... (Tus funciones de Navegación setupBottomNavigation y handleNavigationClick siguen igual) ...
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
            if (item.actionId == "MAP") {
                textView.setTextColor(ContextCompat.getColor(this, R.color.primary_blue))
                iconView.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))
            }
            navItemView.setOnClickListener { handleNavigationClick(item.actionId) }
            container.addView(navItemView)
        }
    }

    private fun handleNavigationClick(action: String) {
        when (action) {
            "HOME" -> { startActivity(Intent(this, MainMenuActivity::class.java)); finish() }
            "MAP" -> Toast.makeText(this, "Estás en el mapa", Toast.LENGTH_SHORT).show()
            "REPORT" -> startActivity(Intent(this, ReportEmergencyActivity::class.java))
            "WHATSAPP_DEV" -> openWhatsappContact("+59170776212", "Hola...")
            "LOGIN" -> Toast.makeText(this, "Login...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWhatsappContact(number: String, message: String) {
        try {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=${number.replace("+", "")}&text=${Uri.encode(message)}")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: Exception) { }
    }
}