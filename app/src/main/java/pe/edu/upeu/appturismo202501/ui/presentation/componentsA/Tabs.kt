package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.R

data class Category(
    val icon: ImageVector,
    val label: String
)



@Composable
fun CategoryTabs(
    categories: List<CategoryResp>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .15f)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor   = Color.Transparent,
        edgePadding      = 15.dp,
        indicator        = {},
        divider          = { Divider(color = dividerColor, thickness = 1.dp) },
        modifier         = modifier
    ) {
        categories.forEachIndexed { index, cat ->
            val isSel = index == selectedIndex

            Tab(
                selected               = isSel,
                onClick                = { onSelected(index) },
                selectedContentColor   = MaterialTheme.colorScheme.primary,
                unselectedContentColor = Color.White,
            ) {
                Surface(
                    color           = if (isSel) Color.White else Color.Transparent,
                    shape           = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    tonalElevation  = if (isSel) 2.dp else 0.dp,
                    shadowElevation = if (isSel) 4.dp else 0.dp
                ) {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier              = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Debug URL de icono
                        LaunchedEffect(cat.iconoUrl) {
                            Log.d("CategoryTabs", "Icono URL = ${cat.iconoUrl}")
                        }

                        // AsyncImage para el icono
                        if (!cat.iconoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model             = cat.iconoUrl,
                                contentDescription = cat.nombre,
                                placeholder        = painterResource(R.drawable.ic_launcher_background),
                                error              = painterResource(R.drawable.ic_launcher_background),
                                modifier           = Modifier.size(24.dp),
                                contentScale       = ContentScale.Crop
                            )
                        }
                        Text(
                            text     = cat.nombre,
                            fontSize = 15.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}