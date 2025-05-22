package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Experience(
    val imageRes: Int,      // R.drawable.midir_
    val tag: String,        // "TICKET DE ENTRADA"
    val title: String,      // "Museo USS Midway"
    val subtitle: String,   // "1 d • Sin colas • Audioguía"
    val rating: Double,     // 4.9
    val reviews: Int,       // 3204
    val price: String       // "39 USD"
)

@Composable
fun ExperienceCard(
    exp: Experience,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier
            .width(260.dp)                      // ≈ captura
            .heightIn(min = 350.dp),
        shape  = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // imagen
            Image(
                painter = painterResource(id = exp.imageRes),
                contentDescription = exp.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            // CONTENIDO
            Column(Modifier.padding(12.dp)) {
                Text(
                    exp.tag.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    exp.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Text(
                    exp.subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    maxLines = 1
                )

                // rating + reviews
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text("${exp.rating}", fontWeight = FontWeight.Bold)
                    Text(" (${exp.reviews})", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    "Desde ${exp.price} por persona",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ExperiencesSection(
    title: String,
    experiences: List<Experience>,
    onMore: () -> Unit = {}
) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp,vertical = 26.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        LazyRow (
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(experiences) { exp ->
                ExperienceCard(exp)
            }
        }
    }
}