package pe.edu.upeu.appturismo202501.data.network

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @ApplicationContext private val ctx: Context
) : Interceptor {

    init {
        // Aseguramos que SessionManager ya esté listo
        SessionManager.init(ctx)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        // 1) Primer intento: SessionManager
        var token = SessionManager.getToken().orEmpty()
        // 2) Si no existe, fallback a TokenUtils
        if (token.isBlank()) {
            token = TokenUtils.TOKEN_CONTENT
        }

        // 3) Construimos la nueva petición
        val builder = original.newBuilder()
            .addHeader("Accept", "application/json")   // importante para JSON

        if (token.isNotBlank()) {
            builder.addHeader("Authorization", "Bearer $token")
        }

        val request = builder.build()
        return chain.proceed(request)
    }
}
