package pe.edu.upeu.appturismo202501.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "user_session"

    // Claves de sesión
    private const val KEY_TOKEN = "key_token"
    private const val KEY_USER_ID = "key_user_id"
    private const val KEY_USER_ROLE = "key_user_role"

    // Claves de preferencias
    private const val KEY_DARK_MODE = "key_dark_mode"
    private const val KEY_COLOR_SCHEME = "key_color_scheme"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ------------------ Sesión ------------------

    fun saveSession(token: String, userId: Int, role: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USER_ROLE, role)
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getUserRole(): String? = prefs.getString(KEY_USER_ROLE, null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    // ------------------ Preferencias ------------------

    fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK_MODE, false)

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun getColorScheme(): String = prefs.getString(KEY_COLOR_SCHEME, "Rojo") ?: "Rojo"

    fun setColorScheme(color: String) {
        prefs.edit().putString(KEY_COLOR_SCHEME, color).apply()
    }

    // ------------------ Utilidad de Sesión ------------------

    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()
}
