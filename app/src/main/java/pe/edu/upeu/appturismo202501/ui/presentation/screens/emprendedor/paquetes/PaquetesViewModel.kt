package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.paquetes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.CreatePaqueteRequest
import pe.edu.upeu.appturismo202501.modelo.PaqueteDto
import pe.edu.upeu.appturismo202501.modelo.TourPackage
import pe.edu.upeu.appturismo202501.modelo.UpdatePaqueteRequest
import pe.edu.upeu.appturismo202501.modelo.toTourPackage
import pe.edu.upeu.appturismo202501.repository.PaqueteRepository
import javax.inject.Inject


@HiltViewModel
class PaquetesViewModel @Inject constructor(
    private val repo: PaqueteRepository
) : ViewModel() {

    // Exponemos directamente UI models
    private val _tourPackages = MutableStateFlow<List<TourPackage>>(emptyList())
    val tourPackages: StateFlow<List<TourPackage>> = _tourPackages

    private val _opResult = MutableSharedFlow<Result<PaqueteDto>>()
    val opResult = _opResult.asSharedFlow()

    private val _currentPackage = MutableStateFlow<PaqueteDto?>(null)
    val currentPackage: StateFlow<PaqueteDto?> = _currentPackage

    private val _deleteResult = MutableSharedFlow<Result<Unit>>()
    val deleteResult: SharedFlow<Result<Unit>> = _deleteResult



    init {
        loadAll()
    }

    fun loadPackageById(id: Long) = viewModelScope.launch {
        repo.getById(id)
            .onSuccess { dto -> _currentPackage.value = dto }
            .onFailure {
                // aquí podrías emitir un error, por ejemplo con otro SharedFlow
            }
    }

    fun loadAll() = viewModelScope.launch {
        val resp = repo.listAll()
        if (resp.isSuccessful) {
            _tourPackages.value = resp.body()
                .orEmpty()
                .map { it.toTourPackage() }
        }
    }

    // si quisieras lista sólo mía
    fun loadPublic() = viewModelScope.launch {
        repo.listAll().let { resp ->
            if (resp.isSuccessful) {
                _tourPackages.value = resp.body()!!.map { it.toTourPackage() }
            }
        }
    }

    fun loadMine() = viewModelScope.launch {
        repo.listMine().onSuccess { dtos ->
            _tourPackages.value = dtos.map { it.toTourPackage() }
        }
    }

    fun createPaquete(req: CreatePaqueteRequest) = viewModelScope.launch {
        val result = runCatching {
            // Esto lanza HttpException si no es 2xx
            repo.create(req).getOrThrow()
        }
        _opResult.emit(result)
        result.onSuccess { loadMine() }
    }



    fun updatePaquete(id: Long, req: UpdatePaqueteRequest) = viewModelScope.launch {
        val res = repo.update(id, req)
        _opResult.emit(res)
        res.onSuccess { loadMine() }
    }

    fun deletePaquete(id: Long) = viewModelScope.launch {
        val result: Result<Unit> = repo.delete(id)
        _deleteResult.emit(result)
        result.onSuccess { loadMine() }
    }
}