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
import android.content.Intent // Importar Intent para abrir WhatsApp/URL
import android.net.Uri // Importar Uri para enlaces
import android.view.LayoutInflater // Importar LayoutInflater para inflar la barra
import android.widget.ImageView // Importar ImageView para los iconos de la barra

class ReportEmergencyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_emergency)

        // Inicializa la fecha y hora
        initDateTime()

        // Configura la navegación inferior y lógica de clics
        setupBottomNavigation() // ¡Ahora esta función existe!

        // Manejar el clic del botón de ubicación (solo Toast por ahora)
        findViewById<Button>(R.id.btnObtenerUbicacion).setOnClickListener {
            // Lógica pendiente: Solicitar permisos y obtener GPS
            Toast.makeText(this, "Simulando obtención de GPS...", Toast.LENGTH_SHORT).show()
            findViewById<TextView>(R.id.tvCoordenadas).text = "Lat: -17.4000, Lon: -66.1500 (Ejemplo)"
        }

        // Manejar el clic del botón de Reportar
        findViewById<Button>(R.id.btnReportar).setOnClickListener {
            // Lógica de envío de datos del formulario
            Toast.makeText(this, "Reporte Enviado. Gracias.", Toast.LENGTH_LONG).show()
            finish() // Cierra la activity y vuelve al MainMenu
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
            NavItem("Registrar Emergencia", R.drawable.sirena, "REPORT"),
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