package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

/* ---------- Modelo mínimo ---------- */
data class CulturalBanner(
    @DrawableRes val image: Int,
    val title: String,
    val subtitle: String

)

/* ---------- Tarjeta ---------- */
@Composable
fun CulturalCard(
    item: CulturalBanner,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        /*  Contenedor que da la posición absoluta  */
        Box(Modifier.fillMaxSize()) {

            /* 1) Imagen de fondo */
            Image(
                painter = painterResource(item.image),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()     // o fillMaxSize()
            )

            /* 2) Rótulos uno debajo del otro, pegados arriba-izquierda */
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)    // ← ya estamos en BoxScope
                    .padding(8.dp)
                    .zIndex(1f)
            ) {
                BannerLabel(item.title)
                Spacer(Modifier.height(4.dp))
                BannerLabel(item.subtitle)
            }
        }
    }
}

@Composable
private fun BannerLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        modifier = modifier
            .background(
                Color(0xCC000000),                 // negro 80 % opaco
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

/* ---------- Sección completa ---------- */
@Composable
fun CulturalSpacesSection(
    title: String,
    items: List<CulturalBanner>,
    modifier: Modifier = Modifier,
    topPadding: Dp = 12.dp,
    bottomPadding: Dp = 8.dp
) {
    Column(modifier) {

        /* TÍTULO con márgenes configurables */
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = topPadding, bottom = bottomPadding)
        )

        /* FILA HORIZONTAL */
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                CulturalCard(item)
            }
        }
    }
}