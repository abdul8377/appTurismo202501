package pe.edu.upeu.appturismo202501

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pe.edu.upeu.appturismo202501.utils.SessionManager

@ExperimentalCoroutinesApi
@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 1) Inicializa el SessionManager con el contexto de la aplicación
        SessionManager.init(this)

        // 2) Verifica en Logcat que no da NPE y muestra el token (null o el valor guardado)
        Log.d("SESSION_INIT", "SessionManager prefs ready — token = ${SessionManager.getToken().orEmpty()}")

        // 3) Aplica automáticamente el tema (claro/oscuro) según la configuración del sistema
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // 4) Inicializa Stripe con tu clave pública
        PaymentConfiguration.init(
            context = applicationContext,
            publishableKey = "pk_test_51RctKqQeKMo8S1cwUdpwO9J5Bnq8eRqUqfklpB6dGHbFaIO8OAttzQGAw8YOMcXXXk9cRhf9becj807eFEQS63fO00UG8pPhSH"  // Usa tu clave pública aquí
        )
    }
}
