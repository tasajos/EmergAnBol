package chakuy.com.vbo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        btnIngresar.setOnClickListener {
            // Aquí irá la navegación a la siguiente pantalla
            // Por ahora solo mostramos un mensaje
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }
}