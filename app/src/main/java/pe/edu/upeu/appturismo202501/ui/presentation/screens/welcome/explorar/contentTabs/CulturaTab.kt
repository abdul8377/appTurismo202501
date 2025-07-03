package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivitiesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalSpacesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.Experience
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ExperiencesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.PaqueteTuristico
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.PaquetesTuristicosSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioPaquete
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel.ZonaTuristicaViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel.PaquetesViewModel

class CulturaTab {
}

@Composable
fun CulturaContent(
    navController: NavController,
    zonaViewModel: ZonaTuristicaViewModel = hiltViewModel(),
    paquetesViewModel: PaquetesViewModel = hiltViewModel()
) {
    val banners    by zonaViewModel.banners.collectAsState(initial = emptyList())
    val paquetes by paquetesViewModel.paquetes.collectAsState()
    val isLoading by paquetesViewModel.isLoading.collectAsState()
    val error by paquetesViewModel.error.collectAsState()

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        when {
            isLoading -> {
                androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            error != null -> {
                Text("Error: $error", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
            else -> {
                PaquetesTuristicosSection(
                    paquetes = paquetes
                )
            }
        }

        // 1️⃣ Experiencias culturales
        val culturalExperiences = listOf(
            Experience(
                R.drawable.ic_launcher_background, "TICKET DE ENTRADA",
                "San Diego Ticket de entrada al Museo USS Midway",
                "1 día • Sin colas • Audioguía opcional", 4.9, 3204, "39 USD"
            ),
            Experience(
                R.drawable.ic_launcher_background, "EXCURSIÓN DE UN DÍA",
                "Las Vegas: Gran Cañón y Presa Hoover, Ópalo",
                "10 horas • Sin colas • Comidas incl.", 4.7, 2105, "99 USD"
            ),
            // …
        )
        ExperiencesSection(
            title = "Experiencias culturales inolvidables",
            experiences = culturalExperiences
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2️⃣ Espacios culturales
        val sample = listOf(
            CulturalBanner(R.drawable.ic_launcher_background, "USS Midway Museum", "46 actividades"),
            CulturalBanner(R.drawable.ic_launcher_background, "Estatua de la Libertad", "164 actividades")
        )
        CulturalSpacesSection(
            title = "Espacios culturales que no te puedes perder",
            items = sample,
            topPadding = 8.dp,
            bottomPadding = 12.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3️⃣ Zonas turísticas
        ActivitiesSection(
            title = "Zonas Turísticas Destacadas",
            items = banners,
            onItemClick = { banner ->
                navController.navigate("zona/${banner.name}")
            }
        )
    }
}

