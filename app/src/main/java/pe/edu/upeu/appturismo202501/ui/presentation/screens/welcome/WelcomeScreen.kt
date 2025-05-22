package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Terrain
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivitiesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivityBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.Category
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CategoryTabs
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalSpacesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.Experience
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ExperiencesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.NavItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SimpleSearchBar
import pe.edu.upeu.appturismo202501.ui.theme.AppTurismo202501Theme
import pe.edu.upeu.appturismo202501.ui.theme.LightGreenColors
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.TurismoNavigationBar
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel.CategoryViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel.ZonaTuristicaViewModel
import pe.edu.upeu.appturismo202501.utils.TokenUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
    zonaViewModel: ZonaTuristicaViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val zonas by zonaViewModel.zonas.collectAsState()
    // Suponiendo que tu ImageResp.fullUrl() ya usa BuildConfig.API_BASE_URL internamente
    val banners: List<ActivityBanner> = zonas.map { zona ->
        val imageUrl = zona.images.firstOrNull()
            ?.fullUrl(TokenUtils.API_URL)     // <— aquí ya usas tu API_URL
            ?: ""                             // o tu placeholder

        ActivityBanner(
            imageUrl = imageUrl,
            name     = zona.nombre
        )
    }

    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val selectedCategory: CategoryResp? = categories.getOrNull(selectedIndex)



    Scaffold(
        bottomBar = {
            var selectedIndex by remember { mutableStateOf(0) }
            val navItems = listOf(
                NavItem("Explorar", Icons.Filled.Place, Icons.Outlined.Place),
                NavItem("Favoritos",   Icons.Filled.Favorite, Icons.Outlined.Favorite),
                NavItem("Carrito",   Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
                NavItem("Reservas", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
                NavItem("Perfil",   Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
            )
            var sel by remember { mutableStateOf(0) }
            TurismoNavigationBar(
                items = navItems,
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        0 -> navController.navigate(Destinations.Welcome.route)
                        1 -> navController.navigate(Destinations.Search.route)
                        4 -> navController.navigate(Destinations.PerfilWelcome.route)
                    }
                }
            )

        }) { padding ->
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

                        // AsyncImage carga fondo
                        AsyncImage(
                            model = selectedCategory?.imagenUrl,
                            contentDescription = selectedCategory?.nombre,
                            placeholder        = painterResource(R.drawable.bg),
                            error              = painterResource(R.drawable.bg),
                            modifier           = Modifier.fillMaxSize(),
                            contentScale       = ContentScale.Crop
                        )
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
                                text       = selectedCategory?.nombre ?: "Cargando...",
                                color      = Color.White,
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text  = selectedCategory?.descripcion ?: "Descripción no disponible",
                                color = Color.White.copy(alpha = .8f)
                            )
                            Spacer(Modifier.height(66.dp))
                        }

                        // ===== TABS =====
                        CategoryTabs(
                            categories    = categories,
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
                        title       = "Zonas Turísticas",
                        items       = banners,
                        onItemClick = { banner ->
                            // navega a detalle
                            navController.navigate("zonaDetail/${banner.name}")
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
    AppTurismo202501Theme(colorScheme = LightGreenColors) {
        WelcomeScreen(navController = navController)
    }
}
