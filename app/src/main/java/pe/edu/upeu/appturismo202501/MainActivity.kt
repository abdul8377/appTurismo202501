package pe.edu.upeu.appturismo202501

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.upeu.appturismo202501.ui.navigation.NavigationHost
import pe.edu.upeu.appturismo202501.ui.theme.*
import pe.edu.upeu.appturismo202501.utils.TokenUtils

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Puedes hacer que el usuario elija esto en el futuro
            val themeType = remember { mutableStateOf(ThemeType.RED) }
            val isDark = isSystemInDarkTheme()

            // Selección de esquema de colores
            val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (isDark) dynamicDarkColorScheme(this) else dynamicLightColorScheme(this)
            } else {
                when (themeType.value) {
                    ThemeType.RED -> if (isDark) DarkRedColors else LightRedColors
                    ThemeType.GREEN -> if (isDark) DarkGreenColors else LightGreenColors
                    ThemeType.PURPLE -> if (isDark) DarkPurpleColors else LightPurpleColors
                }
            }

            // Inicializa contexto para TokenUtils
            TokenUtils.CONTEXTO_APPX = this@MainActivity

            // Aplica el tema global a toda la app
            AppTurismo202501Theme(colorScheme = colorScheme) {
                val navController = rememberNavController()
                val paddingValues = PaddingValues()

                NavigationHost(
                    navController = navController,
                    darkMode = remember { mutableStateOf(isDark) },
                    innerPadding = paddingValues
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTurismo202501Theme(colorScheme = DarkGreenColors) {
        Greeting("Android")
    }
}
