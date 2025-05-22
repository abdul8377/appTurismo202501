package pe.edu.upeu.appturismo202501.utils

object AuthUtils {
    fun isAuthenticated(): Boolean {
        return TokenUtils.TOKEN_CONTENT.isNotEmpty()
    }
}