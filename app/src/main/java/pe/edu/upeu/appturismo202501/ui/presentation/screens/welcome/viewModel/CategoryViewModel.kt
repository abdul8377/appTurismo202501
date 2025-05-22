package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.CategoryDto
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.repository.CategoryRespository
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRespository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryResp>>(emptyList())
    val categories: StateFlow<List<CategoryResp>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = categoryRepository.categoryServices()
                if (response.isSuccessful) {
                    _categories.value = response.body() ?: emptyList()
                } else {
                    println("Error al cargar categorías: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error de conexión: ${e.message}")
            }
        }
    }
}