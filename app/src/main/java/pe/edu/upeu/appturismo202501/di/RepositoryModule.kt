package pe.edu.upeu.appturismo202501.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upeu.appturismo202501.repository.CarritoRepository
import pe.edu.upeu.appturismo202501.repository.CarritoRepositoryImpl
import pe.edu.upeu.appturismo202501.repository.CategoryProductsRepositoryImp
import pe.edu.upeu.appturismo202501.repository.CategoryProductsRespository
import pe.edu.upeu.appturismo202501.repository.CategoryRepositoryImp
import pe.edu.upeu.appturismo202501.repository.CategoryRespository
import pe.edu.upeu.appturismo202501.repository.EmprendimientoRepository
import pe.edu.upeu.appturismo202501.repository.EmprendimientoRepositoryImpl
import pe.edu.upeu.appturismo202501.repository.FavoritoRepository
import pe.edu.upeu.appturismo202501.repository.FavoritoRepositoryImp
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository
import pe.edu.upeu.appturismo202501.repository.LoginUserRespositoryImp
import pe.edu.upeu.appturismo202501.repository.MetodoPagoRepository
import pe.edu.upeu.appturismo202501.repository.MetodoPagoRepositoryImp
import pe.edu.upeu.appturismo202501.repository.ProductoRepositoryImp
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import pe.edu.upeu.appturismo202501.repository.RegisterRepository
import pe.edu.upeu.appturismo202501.repository.RegisterRepositoryImpl
import pe.edu.upeu.appturismo202501.repository.ServicioRepository
import pe.edu.upeu.appturismo202501.repository.ServicioRepositoryImp
import pe.edu.upeu.appturismo202501.repository.TipoDeNegocioRepository
import pe.edu.upeu.appturismo202501.repository.TipoDeNegocioRepositoryImpl
import pe.edu.upeu.appturismo202501.repository.UserRepository
import pe.edu.upeu.appturismo202501.repository.UserRepositoryImpl
import pe.edu.upeu.appturismo202501.repository.ZonaTuristicaRepository
import pe.edu.upeu.appturismo202501.repository.ZonaTuristicaRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginUserRepository(
        loginRepositoryImpl: LoginUserRespositoryImp
    ): LoginUserRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(
        registerRepositoryImpl: RegisterRepositoryImpl
    ): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImp: CategoryRepositoryImp
    ): CategoryRespository

    @Binds
    @Singleton
    abstract fun bindZonaTuristicaRepository(
        impl: ZonaTuristicaRepositoryImpl
    ): ZonaTuristicaRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindTipoDeNegocioRepository(
        tipoDeNegocioRepositoryImpl: TipoDeNegocioRepositoryImpl
    ): TipoDeNegocioRepository

    @Binds
    @Singleton
    abstract fun bindEmprendimientoRepository(
        emprendimientoRepositoryImpl: EmprendimientoRepositoryImpl
    ): EmprendimientoRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(
        productoRepositoryImpl: ProductoRepositoryImp
    ): ProductoRespository

    @Binds
    @Singleton
    abstract fun bindCategoryProductsRepository(
        CategoryProductsRepositoryImpl: CategoryProductsRepositoryImp
    ): CategoryProductsRespository

    @Binds
    @Singleton
    abstract fun bindServicioRepository(
        ServicioRepositoryImpl: ServicioRepositoryImp
    ): ServicioRepository

    // Usamos @Binds para vincular la interfaz con su implementación
    @Binds
    @Singleton
    abstract fun bindMetodoPagoRepository(
        metodoPagoRepositoryImp: MetodoPagoRepositoryImp
    ): MetodoPagoRepository

    // Usamos @Binds para vincular la interfaz con su implementación
    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        carritoRepositoryImpl: CarritoRepositoryImpl
    ): CarritoRepository

}
