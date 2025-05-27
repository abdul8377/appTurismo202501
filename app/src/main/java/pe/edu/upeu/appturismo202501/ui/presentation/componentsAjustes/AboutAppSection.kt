package pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutAppSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Acerca de la App",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Versión: 1.0.0",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Desarrollado por: Equipo AppTurismo",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Contacto: soporte@appturismo.com",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
