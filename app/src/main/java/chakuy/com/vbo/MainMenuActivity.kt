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
        addCategoryIcons() // Llamamos a la nueva función
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerCiudad)
        val ciudades = arrayOf("Seleccionar Ciudad", "La Paz", "Cochabamba", "Santa Cruz")

        val adapter = ArrayAdapter(this, R.layout.item_spinner_elegant, ciudades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val ciudadSeleccionada = ciudades[position]
                if (position > 0) {
                    Toast.makeText(applicationContext, "Has seleccionado: $ciudadSeleccionada", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }
    }

    // Nueva función para añadir los iconos de categorías
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
            // Creamos una CardView para cada elemento
            val cardView = inflater.inflate(R.layout.item_category_icon, container, false) as CardView

            // Accedemos a los elementos dentro de la CardView
            val imageView: ImageView = cardView.findViewById(R.id.category_image)
            val textView: TextView = cardView.findViewById(R.id.category_text)

            imageView.setImageResource(category.imageResId)
            textView.text = category.name
            cardView.setCardBackgroundColor(android.graphics.Color.parseColor(category.bgColor)) // Asigna el color de fondo

            // Hacemos que la tarjeta sea clickeable
            cardView.setOnClickListener {
                Toast.makeText(this, "Clic en ${category.name}", Toast.LENGTH_SHORT).show()
                // Aquí iría la lógica para abrir la pantalla de la categoría seleccionada
            }

            // Añadimos un margen a la derecha para separar las tarjetas
            val layoutParams = cardView.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.category_card_margin) // Define este recurso
            cardView.layoutParams = layoutParams

            container.addView(cardView)
        }
    }
}

// Clase de datos para cada categoría (más fácil de manejar)
data class CategoryItem(val name: String, val imageResId: Int, val bgColor: String)