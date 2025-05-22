package pe.edu.upeu.appturismo202501

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pe.edu.upeu.appturismo202501.utils.isNight
import pe.edu.upeu.appturismo202501.utils.SessionManager  // Importa tu SessionManager

@ExperimentalCoroutinesApi
@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // Inicializa SessionManager con contexto de la app
        SessionManager.init(this)

        val mode = if (isNight()){
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
