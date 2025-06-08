package pe.edu.upeu.appturismo202501.utils

import android.content.Context

object TokenUtils {
    var TOKEN_CONTENT="Aqui va el Token"
    fun clearToken() {
        // Aquí puedes limpiar las preferencias o el token almacenado
        TOKEN_CONTENT = ""
        USER_LOGIN = ""
        ID_ASIS_ACT = 0L
    }

    fun isLoggedIn(): Boolean {
        return TOKEN_CONTENT.isNotEmpty()
    }
    var API_URL="http://192.168.1.2:8000/api/"
    lateinit var CONTEXTO_APPX: Context
    var USER_LOGIN=""
    var ID_ASIS_ACT=0L
}