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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivitiesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivityBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.Experience
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ExperiencesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CategoryTabs
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalBanner
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.CulturalSpacesSection
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SimpleSearchBar
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.SearchScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.AlojamientoContent
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.CulturaContent
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ExperienciasContent
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.GastronomiaContent

import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.GuiasContent
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ProductosContent
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


    val categories by viewModel.categories.collectAsState(initial = emptyList())

    val tabs = listOf(
        Destinations.CulturaTab,
        Destinations.ProductosTab,
        Destinations.GastronomiaTab,
        Destinations.ExperienciasTab,
        Destinations.AlojamientoTab,
        Destinations.GuiasTab,

    )
    // 2) Tu tab estático como CategoryResp "fake"
    val staticTabs = listOf(
        CategoryResp(-1, "Cultura",        "Experiencias culturales",  null, null),
        CategoryResp(-2, "Productos",      "Nuestros mejores productos", null, null)
    )

    // 3) Lista completa de pestañas
    val allTabs = remember(categories) { staticTabs + categories }

    // 4) Estado de cuál pestaña está activa
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    val selectedItem = allTabs.getOrNull(selectedIndex)

    // 5) Creo el NavController anidado
    val innerNav = rememberNavController()

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val screenH = LocalConfiguration.current.screenHeightDp.dp
    val headerH = screenH * 0.65f + topInset

    Scaffold(

    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            LazyColumn {
                item {
                    // ===== HEADER DINÁMICO =====
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(headerH)
                            .offset(y = -topInset)
                    ) {
                        // Log para depurar la URL
                        LaunchedEffect (selectedItem?.imagenUrl) {
                            Log.d("WelcomeScreen", "Imagen URL = ${selectedItem?.imagenUrl}")
                        }

                        // 1) Fondo dinámico
                        if ((selectedItem?.id ?: 0) < 0) {
                            // Imagen local para tabs estáticos
                            val res = when (selectedItem?.id) {
                                -1L -> R.drawable.cultura
                                -2L -> R.drawable.producto

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
                            onSelected    = { idx ->
                                selectedIndex = idx
                                innerNav.navigate(tabs[idx].route) {
                                    popUpTo(innerNav.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            modifier      = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                        )

                    }

                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                    ) {
                        NavHost(
                            navController = innerNav,
                            startDestination = Destinations.CulturaTab.route
                        ) {
                            composable (Destinations.CulturaTab.route) {
                                CulturaContent(navController)
                            }
                            composable (Destinations.ProductosTab.route) {
                                ProductosContent(navController)
                            }
                            composable (Destinations.GastronomiaTab.route) {
                                GastronomiaContent()
                            }
                            composable (Destinations.ExperienciasTab.route){
                                ExperienciasContent()
                            }
                            composable (Destinations.AlojamientoTab.route){
                                AlojamientoContent(navController = navController)

                            }
                            composable (Destinations.GuiasTab.route){
                                GuiasContent()
                            }
                            composable(Destinations.Search.route) {
                                SearchScreen(
                                    navController = navController,
                                    suggestions = listOf("Isla Taquile", "Isla Amantaní", "Lago Titicaca", "Puno", "Cusco")
                                )
                            }


                        }
                    }

                }

            }

            /* ---------- BUSCADOR FLOTANTE ---------- */
            SimpleSearchBar(
                onClick = { navController.navigate(Destinations.Search.route) },
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





