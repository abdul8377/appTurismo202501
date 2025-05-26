package pe.edu.upeu.appturismo202501

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pe.edu.upeu.appturismo202501.utils.SessionManager

@ExperimentalCoroutinesApi
@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializa el SessionManager con el contexto de la aplicación
        SessionManager.init(this)

        // Aplica automáticamente el tema (claro/oscuro) según la configuración del sistema
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
