package chakuy.com.vbo

import android.app.Application
import android.content.res.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializaciones globales de la app
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Manejar cambios de configuración
    }
}