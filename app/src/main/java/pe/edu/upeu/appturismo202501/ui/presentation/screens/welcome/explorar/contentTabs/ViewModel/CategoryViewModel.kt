package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.CategoryProductcsResp

import pe.edu.upeu.appturismo202501.repository.CategoryProductsRespository
import pe.edu.upeu.appturismo202501.repository.CategoryRespository
import javax.inject.Inject

/** UI model más simple si quieres algo específico para la UI */
data class CategoryUi(
    val id: Long,
    val name: String
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repo: CategoryProductsRespository
) : ViewModel() {

    // 1) Flujo privado de DTO puro que vienen del repo
    private val _categories = MutableStateFlow<List< CategoryProductcsResp>>(emptyList())
    val categories: StateFlow<List<CategoryProductcsResp>> = _categories.asStateFlow()

    // 2) (Opcional) Flujo de UI models listos para la UI, mapeo directo
    val categoriesUi: StateFlow<List<CategoryUi>> = categories
        .map { list ->
            list.map { resp ->
                CategoryUi(
                    id   = resp.id,
                    name = resp.nombre
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        loadCategories()
    }

    /** Lanza la petición al repo para cargar las categorías */
    fun loadCategories() = viewModelScope.launch {
        val resp = repo.categoryProductsServices()
        if (resp.isSuccessful) {
            _categories.value = resp.body().orEmpty()
        } else {
            // TODO: manejo de error (logging, Snackbar, retry, etc.)
        }
    }
}