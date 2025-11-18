package chakuy.com.vbo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Configuración básica del Spinner (Opcional por ahora)
        // setupSpinner()
    }

    /* Ejemplo de como cargar datos al Spinner de Ciudad más adelante
    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerCiudad) // Asegúrate de ponerle ID al spinner en el XML
        val ciudades = arrayOf("Cochabamba", "Sacaba", "Quillacollo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ciudades)
        spinner.adapter = adapter
    }
    */
}