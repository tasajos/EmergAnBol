package chakuy.com.vbo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear TextView programáticamente para evitar problemas con view binding
        val textView = TextView(this).apply {
            text = "Emergencia Bolivia Central"
            textSize = 20f
            setPadding(50, 50, 50, 50)
        }

        setContentView(textView)
    }
}