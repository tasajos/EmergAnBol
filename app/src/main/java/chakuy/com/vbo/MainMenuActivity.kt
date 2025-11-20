package chakuy.com.vbo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.content.Intent
import android.net.Uri
import android.widget.ScrollView

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setupSpinner()
        addCategoryIcons() // Los iconos cuadrados de arriba
        addInfoCards()     // Las nuevas tarjetas rectangulares de abajo
        addGroupCards() //
        setupBottomNavigation() // <-- ¡Añade esta línea!
    }

    private fun setupSpinner() {
        // ... (Tu código del spinner se mantiene igual) ...
        val spinner: Spinner = findViewById(R.id.spinnerCiudad)
        val ciudades = arrayOf("Seleccionar Ciudad", "La Paz", "Cochabamba", "Santa Cruz")
        val adapter = ArrayAdapter(this, R.layout.item_spinner_elegant, ciudades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // ... (Tu función addCategoryIcons se mantiene igual) ...
    private fun addCategoryIcons() {
        val container: LinearLayout = findViewById(R.id.category_icons_container)
        val categories = listOf(
            CategoryItem("Bomberos Voluntarios", R.drawable.bomberos, "#669966"), // Reemplaza con tus PNGs

            CategoryItem("Ambulancias", R.drawable.ambu, "#5DADE2"),

            CategoryItem("Hospitales", R.drawable.hospital, "#ED8A54"),

            CategoryItem("Animalistas", R.drawable.huella, "#4A6E8C"),

            CategoryItem("Ambientalistas", R.drawable.ambientalismo, "#DC3545"),

            CategoryItem("Educacion", R.drawable.edu, "#FFC107"),

            CategoryItem("Canal Whatsapp", R.drawable.whats, "#99A3A4") // Asegúrate de tener al menos 6-8 para ver el desplazamiento

        )

        val inflater = LayoutInflater.from(this)

        for (category in categories) {
            val cardView = inflater.inflate(R.layout.item_category_icon, container, false) as CardView
            val imageView: ImageView = cardView.findViewById(R.id.category_image)
            val textView: TextView = cardView.findViewById(R.id.category_text)

            imageView.setImageResource(category.imageResId)
            textView.text = category.name
            cardView.setCardBackgroundColor(android.graphics.Color.parseColor(category.bgColor))

            val layoutParams = cardView.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.category_card_margin)
            cardView.layoutParams = layoutParams





            cardView.setOnClickListener {
                if (category.name == "Bomberos Voluntarios") {
                    // Si es Bomberos, abrimos la nueva Activity
                    val intent = Intent(this, BomberosListActivity::class.java)
                    startActivity(intent)
                } else if (category.name == "Ambulancias") {
                    // ABRIR AMBULANCIAS
                    startActivity(Intent(this, AmbulanciasListActivity::class.java))
                } else if (category.name == "Animalistas") {
                    // ABRIR ANIMALISTAS
                    startActivity(Intent(this, AnimalistasListActivity::class.java))
                }else {
                    Toast.makeText(this, "Click en ${category.name}", Toast.LENGTH_SHORT).show()
                }
            }
            container.addView(cardView)
        }
    }


    // --- NUEVA FUNCIÓN PARA INFORMACIÓN ÚTIL ---
    private fun addInfoCards() {
        val container: LinearLayout = findViewById(R.id.info_cards_container)

        // Define aquí tus datos: Título, Descripción, Icono
        val infoItems = listOf(
            InfoItem(
                "Kits de Emergencia",
                "Herramientas e insumos necesarios para eventos adversos.",
                R.drawable.kitdesupervivencia // icono de maletín de emergencia
            ),
            InfoItem(
                "Eventos",
                "Talleres, simulacros y capacitaciones cercanas.",
                R.drawable.evento // Icono de calendario/lista
            ),
            InfoItem(
                "Voluntariado",
                "Oportunidades para unirte a equipos de primera respuesta.",
                R.drawable.caridad // Icono de persona o corazón
            ),
            InfoItem(
                "Noticias",
                "Últimas actualizaciones sobre emergencias en el país.",
               R.drawable.reportero // Icono de periódico/visión
            )
        )

        val inflater = LayoutInflater.from(this)

        for (item in infoItems) {
            val cardView = inflater.inflate(R.layout.item_info_card, container, false) as CardView

            val titleView: TextView = cardView.findViewById(R.id.info_title)
            val descView: TextView = cardView.findViewById(R.id.info_desc)
            val iconView: ImageView = cardView.findViewById(R.id.info_icon)

            titleView.text = item.title
            descView.text = item.desc
            iconView.setImageResource(item.iconResId)

            // Si el icono es un Vector Drawable, puedes teñirlo aquí para un color uniforme:
            // iconView.setColorFilter(getColor(R.color.primary_blue))

            cardView.setOnClickListener {
                Toast.makeText(this, "Abriendo: ${item.title}", Toast.LENGTH_SHORT).show()
            }

            container.addView(cardView)
        }
    }


    // --- NUEVA FUNCIÓN PARA GRUPOS DE INFORMACIÓN ---
    private fun addGroupCards() {
        val container: LinearLayout = findViewById(R.id.group_cards_container)

        // Define la URL del grupo de emergencia aquí
        val EMERGENCY_GROUP_URL = "https://chat.whatsapp.com/BHSQ5odTk61ARnyNJ3Kexl?mode=hqrt2"
        val WHATSAPP_NUMBER = "+59170776212"
        val CONTACT_MESSAGE = "Quiero consultar acerca de..."

        // Definición de las 3 tarjetas de grupo
        val groupItems = listOf(
            GroupItem("Grupo de Emergencia WhatsApp", R.drawable.whats),
            GroupItem("Grupo de Información General", R.drawable.whats),
            GroupItem("Contáctanos", R.drawable.whats) // El botón que vamos a modificar
        )

        val inflater = LayoutInflater.from(this)

        for (item in groupItems) {
            val cardView = inflater.inflate(R.layout.item_group_card, container, false) as CardView
            val titleView: TextView = cardView.findViewById(R.id.group_title)
            val iconView: ImageView = cardView.findViewById(R.id.group_icon)

            titleView.text = item.title
            iconView.setImageResource(item.iconResId)

            cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#7986CB"))

            // --- LÓGICA CLICKEABLE CON CASOS DIFERENCIADOS ---
            cardView.setOnClickListener {
                when (item.title) {
                    "Grupo de Emergencia WhatsApp" -> {
                        openUrl(EMERGENCY_GROUP_URL) // Abre el enlace de invitación
                    }
                    "Contáctanos" -> {
                        openWhatsappContact(WHATSAPP_NUMBER, CONTACT_MESSAGE) // Abre chat directo
                    }
                    else -> { // Para "Grupo de Información General"
                        Toast.makeText(this, "Abriendo enlace para: ${item.title}", Toast.LENGTH_SHORT).show()
                        // Aquí puedes poner otro enlace de grupo si tienes uno.
                    }
                }
            }
            // --- FIN LÓGICA CLICKEABLE MODIFICADA ---

            container.addView(cardView)
        }
    }


    // --- FUNCIÓN PARA ABRIR CUALQUIER URL ---
    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: No se pudo abrir la aplicación o el enlace.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    // --- FUNCIÓN PARA ABRIR WHATSAPP ---


    private fun openWhatsappContact(number: String, message: String) {
        try {
            // El número debe estar en formato internacional sin '+'
            val numberCleaned = number.replace("+", "")

            // Codificar el mensaje para la URL
            val encodedMessage = Uri.encode(message)

            // Construir la URI para WhatsApp
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$numberCleaned&text=$encodedMessage")

            // Crear el Intent para abrir el navegador o la aplicación WhatsApp
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Intent.ACTION_VIEW usa la aplicación predeterminada (WhatsApp) o el navegador
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir WhatsApp.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun setupBottomNavigation() {
        val container: LinearLayout = findViewById(R.id.bottom_nav_container)
        val inflater = LayoutInflater.from(this)

        // IMPORTANTE: Asegúrate de tener estos drawables (PNG) en res/drawable/
        val navItems = listOf(
            NavItem("Inicio", R.drawable.hogar, "HOME"),
            NavItem("Mapa", R.drawable.ubicaciones, "MAP"),
            NavItem("Emergencia", R.drawable.sirena, "REPORT"),
            NavItem("WhatsApp", R.drawable.whats, "WHATSAPP_DEV"),
            NavItem("Login", R.drawable.seguridad, "LOGIN")
        )

        for (item in navItems) {
            // Inflar el diseño de item_bottom_nav.xml
            val navItemView = inflater.inflate(R.layout.item_bottom_nav, container, false) as LinearLayout

            // Buscar elementos dentro del item
            val iconView: ImageView = navItemView.findViewById(R.id.nav_icon)
            val textView: TextView = navItemView.findViewById(R.id.nav_text)

            // Asignar datos
            iconView.setImageResource(item.iconResId)
            textView.text = item.title

            // Lógica de clic
            navItemView.setOnClickListener {
                handleNavigationClick(item.actionId)
            }

            container.addView(navItemView)
        }
    }

    private fun handleNavigationClick(action: String) {
        when (action) {
            "HOME" -> {
                // Si esta es la MainMenuActivity, simplemente hacemos scroll al inicio
                val scrollView: ScrollView = findViewById(R.id.scrollView) // Asumiendo que tu ScrollView tiene este ID
                scrollView.fullScroll(ScrollView.FOCUS_UP)
                Toast.makeText(this, "Volviendo a la página principal.", Toast.LENGTH_SHORT).show()
            }
            "MAP" -> {
                startActivity(Intent(this, MapsActivity::class.java))
                // Aquí iría el Intent para abrir MapasActivity
            }
            "REPORT" -> {
                // Abre ReportEmergencyActivity
                val intent = Intent(this, ReportEmergencyActivity::class.java)
                startActivity(intent)
            }
            "WHATSAPP_DEV" -> {
                // Usamos la función ya creada
                openWhatsappContact("+59170776212", "Hola, quiero contactar al equipo de desarrollo acerca de la app...")
            }
            "LOGIN" -> {
                Toast.makeText(this, "Abriendo pantalla de inicio de sesión.", Toast.LENGTH_SHORT).show()
                // Aquí iría el Intent para abrir LoginActivity
            }
        }
    }

}
// Clases de datos al final del archivo
//data class CategoryItem(val name: String, val imageResId: Int, val bgColor: String)
//data class InfoItem(val title: String, val desc: String, val iconResId: Int)
//data class GroupItem(val title: String, val iconResId: Int)
//data class NavItem(val title: String, val iconResId: Int, val actionId: String)