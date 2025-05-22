package pe.edu.upeu.appturismo202501.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pe.edu.upeu.appturismo202501.data.remote.RestCategory
import pe.edu.upeu.appturismo202501.data.remote.RestLoginUsuario
import pe.edu.upeu.appturismo202501.data.remote.RestRegister
import pe.edu.upeu.appturismo202501.data.remote.RestZonaTuristica
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    //api rest
    var retrofit: Retrofit? = null

    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = TokenUtils.API_URL


    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseUrl: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()

                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(baseUrl).build()
        }
        return retrofit!!
    }

    @Singleton
    @Provides
    fun restLoginUser(retrofit: Retrofit): RestLoginUsuario {
        return retrofit.create(RestLoginUsuario::class.java)
    }
    @Singleton
    @Provides
    fun restRegister(retrofit: Retrofit): RestRegister {
        return retrofit.create(RestRegister::class.java)
    }
    @Singleton
    @Provides
    fun restCategory(retrofit: Retrofit): RestCategory {
        return retrofit.create(RestCategory::class.java)
    }
    @Singleton
    @Provides
    fun provideRestZonaTuristica(retrofit: Retrofit): RestZonaTuristica =
        retrofit.create(RestZonaTuristica::class.java)


}