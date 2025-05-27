package pe.edu.upeu.appturismo202501.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pe.edu.upeu.appturismo202501.data.remote.*
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

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

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
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
    fun provideRestZonaTuristica(retrofit: Retrofit): RestZonaTuristica {
        return retrofit.create(RestZonaTuristica::class.java)
    }

    @Singleton
    @Provides
    fun provideRestUser(retrofit: Retrofit): RestUser {
        return retrofit.create(RestUser::class.java)
    }

    @Singleton
    @Provides
    fun restTipoDeNegocio(retrofit: Retrofit): RestTipoDeNegocio {
        return retrofit.create(RestTipoDeNegocio::class.java)
    }

    @Singleton
    @Provides
    fun provideRestEmprendimiento(retrofit: Retrofit): RestEmprendimiento {
        return retrofit.create(RestEmprendimiento::class.java)
    }


    @Singleton
    @Provides
    fun provideRestProducto(retrofit: Retrofit): RestProductos {
        return retrofit.create(RestProductos::class.java)
    }

    @Singleton
    @Provides
    fun provideRestCategoryProducts(retrofit: Retrofit): RestCategoryProducts {
        return retrofit.create(RestCategoryProducts::class.java)
    }
}



