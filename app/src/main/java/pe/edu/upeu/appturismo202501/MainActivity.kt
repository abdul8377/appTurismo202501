package pe.edu.upeu.appturismo202501

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.upeu.appturismo202501.ui.navigation.NavigationHost
import pe.edu.upeu.appturismo202501.ui.theme.AppTurismo202501Theme
import pe.edu.upeu.appturismo202501.ui.theme.DarkGreenColors
import pe.edu.upeu.appturismo202501.ui.theme.DarkPurpleColors
import pe.edu.upeu.appturismo202501.ui.theme.DarkRedColors
import pe.edu.upeu.appturismo202501.ui.theme.LightGreenColors
import pe.edu.upeu.appturismo202501.ui.theme.LightPurpleColors
import pe.edu.upeu.appturismo202501.ui.theme.LightRedColors
import pe.edu.upeu.appturismo202501.ui.theme.ThemeType
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import pe.edu.upeu.appturismo202501.utils.isNight


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeType= remember{ mutableStateOf(ThemeType.RED) }
            val darkThemex= isNight()
            val darkTheme = remember { mutableStateOf(darkThemex) }
            val colorScheme=when(themeType.value){
                ThemeType.PURPLE->{if (darkTheme.value)
                    DarkPurpleColors
                else LightPurpleColors}
                ThemeType.RED->{if (darkTheme.value) DarkRedColors
                else
                    LightRedColors}ThemeType.GREEN->{if (darkTheme.value) DarkGreenColors
                else LightGreenColors}
                else->{LightRedColors}
            }

            TokenUtils.CONTEXTO_APPX=this@MainActivity

            AppTurismo202501Theme(colorScheme = colorScheme) {
                val navController = rememberNavController()
                val darkTheme = remember { mutableStateOf(isNight()) }
                val paddingValues = PaddingValues() // Puedes ajustar esto si necesitas un padding específico
                NavigationHost(
                    navController = navController,
                    darkMode = darkTheme,
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
    AppTurismo202501Theme (colorScheme = DarkGreenColors){
        Greeting("Android")
    }
}

