package pe.edu.upeu.appturismo202501.data.network

import okhttp3.Interceptor
import okhttp3.Response
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = SessionManager.getToken().orEmpty()
        val request = if (token.isNotEmpty()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        return chain.proceed(request)
    }
}
