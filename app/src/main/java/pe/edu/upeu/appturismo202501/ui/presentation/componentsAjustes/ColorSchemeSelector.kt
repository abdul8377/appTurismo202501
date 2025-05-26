package pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorSchemeSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    val colorOptions = listOf(
        "Rojo" to Color(0xFFD32F2F),
        "Verde" to Color(0xFF388E3C),
        "Púrpura" to Color(0xFF7B1FA2)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Esquema de Color",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colorOptions.forEach { (label, color) ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = CircleShape)
                        .border(
                            width = if (label == selectedColor) 4.dp else 2.dp,
                            color = if (label == selectedColor) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(label) },
                    contentAlignment = Alignment.Center
                ) {}
            }
        }
    }
}
