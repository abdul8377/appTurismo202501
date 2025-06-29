package pe.edu.upeu.appturismo202501.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.edu.upeu.appturismo202501.data.local.storage.CarritoLocalStorage
import pe.edu.upeu.appturismo202501.data.network.AuthInterceptor
import pe.edu.upeu.appturismo202501.data.remote.*
import pe.edu.upeu.appturismo202501.repository.FavoritoRepository
import pe.edu.upeu.appturismo202501.repository.FavoritoRepositoryImp
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    @Named("BaseUrl")
    fun provideBaseUrl(): String = TokenUtils.API_URL

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        logging: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        @Named("BaseUrl") baseUrl: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(baseUrl)
            .build()

    @Provides @Singleton
    fun provideRestLoginUser(retrofit: Retrofit): RestLoginUsuario =
        retrofit.create(RestLoginUsuario::class.java)

    @Provides @Singleton
    fun provideRestRegister(retrofit: Retrofit): RestRegister =
        retrofit.create(RestRegister::class.java)

    @Provides @Singleton
    fun provideRestCategory(retrofit: Retrofit): RestCategory =
        retrofit.create(RestCategory::class.java)

    @Provides @Singleton
    fun provideRestZonaTuristica(retrofit: Retrofit): RestZonaTuristica =
        retrofit.create(RestZonaTuristica::class.java)

    @Provides @Singleton
    fun provideRestUser(retrofit: Retrofit): RestUser =
        retrofit.create(RestUser::class.java)

    @Provides @Singleton
    fun provideRestTipoDeNegocio(retrofit: Retrofit): RestTipoDeNegocio =
        retrofit.create(RestTipoDeNegocio::class.java)

    @Provides @Singleton
    fun provideRestEmprendimiento(retrofit: Retrofit): RestEmprendimiento =
        retrofit.create(RestEmprendimiento::class.java)

    @Provides @Singleton
    fun provideRestProductos(retrofit: Retrofit): RestProductos =
        retrofit.create(RestProductos::class.java)

    @Provides @Singleton
    fun provideRestCategoryProducts(retrofit: Retrofit): RestCategoryProducts =
        retrofit.create(RestCategoryProducts::class.java)

    @Provides @Singleton
    fun provideRestServicio(retrofit: Retrofit): RestServicio =
        retrofit.create(RestServicio::class.java)

    @Provides @Singleton
    fun provideRestFavorito(retrofit: Retrofit): RestFavorito =
        retrofit.create(RestFavorito::class.java)

    @Provides
    @Singleton
    fun provideRestMetodoPago(retrofit: Retrofit): RestMetodoPago =
        retrofit.create(RestMetodoPago::class.java)

    @Provides
    @Singleton
    fun provideRestCarrito(retrofit: Retrofit): RestCarrito =
        retrofit.create(RestCarrito::class.java)

    @Provides
    @Singleton
    fun provideCarritoLocalStorage(
        @ApplicationContext context: Context
    ): CarritoLocalStorage = CarritoLocalStorage(context)

    @Provides
    @Singleton
    fun provideFavoritoRepository(
        impl: FavoritoRepositoryImp
    ): FavoritoRepository = impl
}
