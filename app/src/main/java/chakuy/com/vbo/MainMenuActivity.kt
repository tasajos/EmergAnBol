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

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setupSpinner()
        addCategoryIcons() // Los iconos cuadrados de arriba
        addInfoCards()     // Las nuevas tarjetas rectangulares de abajo
        addGroupCards() // <-- ¡Añade esta línea!
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

            cardView.setOnClickListener { Toast.makeText(this, category.name, Toast.LENGTH_SHORT).show() }
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

        // Definición de las 3 tarjetas de grupo
        val groupItems = listOf(
            GroupItem("Grupo de Emergencia WhatsApp", R.drawable.whats), // Debes tener este PNG/Vector
            GroupItem("Grupo de Información General", R.drawable.whats), // Debes tener este PNG/Vector
            GroupItem("Contáctanos", R.drawable.whats) // Debes tener este PNG/Vector
        )

        val inflater = LayoutInflater.from(this)

        for (item in groupItems) {
            // Inflamos el layout de grupo
            val cardView = inflater.inflate(R.layout.item_group_card, container, false) as CardView

            val titleView: TextView = cardView.findViewById(R.id.group_title)
            val iconView: ImageView = cardView.findViewById(R.id.group_icon)

            titleView.text = item.title
            iconView.setImageResource(item.iconResId)

            // Opcional: Cambiar color de fondo si lo deseas, usando un color de tu colors.xml
            cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#7986CB")) // Usando coord_blue

            cardView.setOnClickListener {
                Toast.makeText(this, "Abriendo enlace para: ${item.title}", Toast.LENGTH_SHORT).show()
                // Aquí iría el Intent para abrir el enlace de WhatsApp o la pantalla de Contacto
            }

            container.addView(cardView)
        }
    }

}
// Clases de datos al final del archivo
data class CategoryItem(val name: String, val imageResId: Int, val bgColor: String)
data class InfoItem(val title: String, val desc: String, val iconResId: Int)
data class GroupItem(val title: String, val iconResId: Int)