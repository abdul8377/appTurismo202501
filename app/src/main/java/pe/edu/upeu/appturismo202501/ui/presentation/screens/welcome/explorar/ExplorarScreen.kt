package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivitiesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivityBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.Experience
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ExperiencesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CategoryTabs
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalSpacesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SimpleSearchBar
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel.CategoryViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel.ZonaTuristicaViewModel
import pe.edu.upeu.appturismo202501.ui.theme.AppTurismo202501Theme
import pe.edu.upeu.appturismo202501.ui.theme.LightGreenColors
import pe.edu.upeu.appturismo202501.utils.TokenUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorarScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
    zonaViewModel: ZonaTuristicaViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()

    val banners by zonaViewModel.banners.collectAsState()
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val selectedCategory: CategoryResp? = categories.getOrNull(selectedIndex)


    val staticTab = CategoryResp(
        id         = -1L,
        nombre     = "Cultura Estatico",
        descripcion= "Experiencias culturales estatico", // opcional
        imagenUrl  = null,
        iconoUrl   = null
    )
    val allTabs = remember(categories) {
        listOf(staticTab) + categories
    }
    val selectedItem = allTabs.getOrNull(selectedIndex)

    Scaffold(
        bottomBar = {
            // En ExplorarScreen normalmente no va barra inferior, la maneja WelcomeMain
            // Si quieres, aquí puedes poner otra barra o nada
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            LazyColumn {
                item {
                    // ===== HEADER DINÁMICO =====
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height((LocalConfiguration.current.screenHeightDp * 0.65f).dp)
                    ) {
                        // Log para depurar la URL
                        LaunchedEffect (selectedCategory?.imagenUrl) {
                            Log.d("WelcomeScreen", "Imagen URL = ${selectedCategory?.imagenUrl}")
                        }

                        // 1) Fondo dinámico
                        if ((selectedItem?.id ?: 0) < 0) {
                            // Imagen local para tabs estáticos
                            val res = when (selectedItem?.id) {
                                -1L -> R.drawable.cultura

                                else-> R.drawable.bg
                            }
                            Image(
                                painter = painterResource(res),
                                contentDescription = selectedItem?.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Imagen remota para tabs de la API
                            AsyncImage(
                                model = selectedItem?.imagenUrl,
                                contentDescription = selectedItem?.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.bg),
                                error       = painterResource(R.drawable.bg)
                            )
                        }
                        // 2) Degradado superpuesto
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.6f)
                                        ),
                                        startY = 0f,
                                        endY = Float.POSITIVE_INFINITY
                                    )
                                )
                        )

                        // Nombre y descripción sobre la imagen
                        Column(
                            Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text       = selectedItem?.nombre.orEmpty(),
                                color      = Color.White,
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text  = selectedItem?.descripcion.orEmpty(),
                                color = Color.White.copy(alpha = .8f)
                            )
                            Spacer(Modifier.height(66.dp))
                        }

                        // ===== TABS =====
                        CategoryTabs(
                            categories    = allTabs,
                            selectedIndex = selectedIndex,
                            onSelected    = { idx -> selectedIndex = idx },
                            modifier      = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                        )

                    }
                }

                item {


                    val culturalExperiences = listOf(
                        Experience(R.drawable.ic_launcher_background, "TICKET DE ENTRADA",
                            "San Diego Ticket de entrada al Museo USS Midway",
                            "1 día • Sin colas • Audioguía opcional", 4.9, 3204, "39 USD"),
                        Experience(R.drawable.ic_launcher_background, "EXCURSIÓN DE UN DÍA",
                            "Las Vegas: Gran Cañón y Presa Hoover, Ópalo",
                            "10 horas • Sin colas • Comidas incl.", 4.7, 2105, "99 USD"),
                        // …más
                    )

                    ExperiencesSection(
                        title = "Experiencias culturales inolvidables",
                        experiences = culturalExperiences
                    )
                }

                val sample = listOf(
                    CulturalBanner(R.drawable.ic_launcher_background,  "USS Midway Museum",    "46 actividades"),
                    CulturalBanner(R.drawable.ic_launcher_background,  "Estatua de la Libertad","164 actividades")
                )

                item {
                    CulturalSpacesSection(
                        title = "Espacios culturales que no te puedes perder",
                        items = sample,
                        topPadding = 8.dp,      // separa un poco del bloque anterior
                        bottomPadding = 12.dp   // separa del LazyRow
                    )
                }

                item {
                    ActivitiesSection(
                        title = "Zonas Turísticas Destacadas",
                        items = banners,
                        onItemClick = { banner ->
                            // por ejemplo, navegar a un detalle:
                            navController.navigate("zona/${banner.name}")
                        }
                    )
                }






            }

            /* ---------- BUSCADOR FLOTANTE ---------- */
            SimpleSearchBar(
                onClick = { navController.navigate("search") },
                modifier = Modifier
                    //.align(Alignment.TopCenter)   // encima del resto
                    .align(Alignment.TopCenter)
                    .padding(top = 35.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp)         // distancia al borde
                    .zIndex(1f)                   // garantiza prioridad de dibujo
            )
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    val navController = rememberNavController()          // ← NavController “falso” para previews
    AppTurismo202501Theme (colorScheme = LightGreenColors) {
        ExplorarScreen(navController = navController)
    }
}
