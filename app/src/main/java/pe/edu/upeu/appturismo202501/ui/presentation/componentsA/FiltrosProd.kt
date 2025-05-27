package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.CategoryUi

/**
 * Un panel expandible genérico: muestra un título clickeable y expande/cierra su contenido.
 */
@Composable
fun ExpandableFilter(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
        }
        AnimatedVisibility (
            visible = expanded,
            enter = fadeIn(tween(200)),
            exit  = fadeOut(tween(200))
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                content()
            }
        }
        Divider()
    }
}

/**
 * Filtros específicos: categorías, precio y calificaciones.
 * - categories: lista de nombres de categoría
 * - onCategorySelected: callback
 * - onPriceSelected: callback de etiqueta de rango
 * - onRatingSelected: callback de rating (1–5)
 */
@Composable
fun ProductsFilterPanel(
    categories: List<CategoryUi>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit,
    minPrice: String,
    onMinPriceChange: (String) -> Unit,
    maxPrice: String,
    onMaxPriceChange: (String) -> Unit,
    selectedRating: Int?,
    onRatingSelected: (Int?) -> Unit
) {
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedPrice    by remember { mutableStateOf(false) }
    var expandedRating   by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Filtros",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        Divider()

        // Categorías
        ExpandableFilter(
            title = "Categorías",
            expanded = expandedCategory,
            onToggle = { expandedCategory = !expandedCategory }
        ) {
            Column {
                categories.forEach { cat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = (selectedCategoryId == cat.id),
                                onValueChange = { checked ->
                                    onCategorySelected(if (checked) cat.id else null)
                                }
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedCategoryId == cat.id),
                            onClick = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(cat.name, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Precio
        ExpandableFilter(
            title    = "Precio",
            expanded = expandedPrice,
            onToggle = { expandedPrice = !expandedPrice }
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = minPrice,
                    onValueChange = onMinPriceChange,
                    label = { Text("Mínimo") },
                    placeholder = { Text("S/") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = maxPrice,
                    onValueChange = onMaxPriceChange,
                    label = { Text("Máximo") },
                    placeholder = { Text("S/") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }


        // Calificaciones
        ExpandableFilter(
            title = "Calificaciones",
            expanded = expandedRating,
            onToggle = { expandedRating = !expandedRating }
        ) {
            Row {
                (1..5).forEach { star ->
                    IconToggleButton(
                        checked = (selectedRating ?: 0) >= star,
                        onCheckedChange = { checked ->
                            onRatingSelected(if (checked) star else null)
                        }
                    ) {
                        Icon(
                            imageVector = if ((selectedRating ?: 0) >= star) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if ((selectedRating ?: 0) >= star)
                                MaterialTheme.colorScheme.primary
                            else Color.Gray
                        )
                    }
                }
            }
        }
    }
}