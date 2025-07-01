package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.detalleServicios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.AlojamientoViewModel


data class Comment(
    val id: Int,
    val userName: String,
    val userImage: Int,
    val comment: String,
    val date: String,
    val likes: Int,
    val replies: List<Reply> = emptyList()
)

data class Reply(
    val id: Int,
    val userName: String,
    val userImage: Int,
    val comment: String,
    val date: String,
    val likes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlojamientoDetailScreen(
    servicioId: Long,
    navController: NavHostController,
    viewModel: AlojamientoViewModel = hiltViewModel()
) {

    val comments = listOf(
        Comment(
            id = 1,
            userName = "María Rodríguez",
            userImage = R.drawable.cultura,
            comment = "¡Una experiencia increíble! Los guías fueron muy profesionales y el paisaje es simplemente espectacular.",
            date = "Hace 2 semanas",
            likes = 42,
            replies = listOf(
                Reply(
                    id = 101,
                    userName = "Aventuras Andinas",
                    userImage = R.drawable.cultura,
                    comment = "¡Gracias por tu comentario María! Nos alegra que hayas disfrutado la experiencia.",
                    date = "Hace 2 semanas",
                    likes = 8
                )
            )
        ),
        Comment(
            id = 2,
            userName = "Carlos Martínez",
            userImage = R.drawable.producto,
            comment = "Muy bien organizado. La comida local fue lo mejor. Recomendado 100%",
            date = "Hace 1 mes",
            likes = 31
        ),
        Comment(
            id = 3,
            userName = "Ana López",
            userImage = R.drawable.cultura,
            comment = "El tour superó mis expectativas. Volvería a hacerlo sin dudarlo.",
            date = "Hace 3 meses",
            likes = 28,
            replies = listOf(
                Reply(
                    id = 301,
                    userName = "Luis García",
                    userImage = R.drawable.producto,
                    comment = "Totalmente de acuerdo, Ana. Fue una experiencia inolvidable.",
                    date = "Hace 3 meses",
                    likes = 5
                )
            )
        )
    )

    // 1. Estado que recoge el detalle de tu servicio
    val detalleUi by viewModel
        .fetchAlojamientoDetalle(servicioId)
        .collectAsState(initial = null)

    // 2. Mientras carga, mostramos indicador
    if (detalleUi == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FC)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 3. Una vez viene el detalle…
    val detalle = detalleUi!!
// si DETAIL.images es List<ImagenDto>?
    val dtoImages = detalle.images ?: emptyList()

    // 4. Extraemos lista de imágenes (aquí solo tienes una; si tu DTO trae varias, adapta)
    val serviceImages = dtoImages
        .map { it.url }
        .ifEmpty { listOf("https://via.placeholder.com/300") }
    var currentImageIndex by remember { mutableStateOf(0) }
    var isLiked by remember { mutableStateOf(detalle.isFavorite) }
    var currentLikes by remember { mutableStateOf(0) }
    var backClicked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles del Servicio",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!backClicked) {
                                backClicked = true
                                navController.popBackStack()
                            }
                        },
                        enabled = !backClicked          // además deshabilita la pulsación doble
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isLiked = !isLiked
                        currentLikes = if (isLiked) currentLikes + 1 else currentLikes - 1
                    }) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Me gusta",
                            tint = if (isLiked) Color(0xFFFF5252) else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xCC000000), Color(0x00000000))
                    )
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Abrir chat */ },
                containerColor = Color(0xFF4CAF50),
                modifier = Modifier
                    .padding(bottom = 100.dp)
                    .shadow(12.dp, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Chat",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .shadow(elevation = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1/2 pantalla para el precio
                    Column(
                        modifier = Modifier
                            .weight(1f),            // ocupa mitad del ancho
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Precio desde",
                            color = Color(0xFF757575),
                            fontSize = 12.sp
                        )
                        Text(
                            text = detalle.priceFormatted,
                            style = TextStyle(
                                fontSize = 18.sp,     // antes 22.sp, ahora más pequeño
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2196F3)
                            )
                        )
                    }

                    // 1/2 pantalla para el botón
                    Button(
                        onClick = { /* Verificar disponibilidad */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)            // ocupa la otra mitad
                            .height(55.dp),
                        shape = RoundedCornerShape(15.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            "Verificar disponibilidad",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FC))
        ) {
            // Carrusel de imágenes
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    // Imagen actual
                    AsyncImage(
                        model = serviceImages[currentImageIndex],
                        contentDescription = detalle.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Indicadores de imágenes
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        serviceImages.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(if (currentImageIndex == index) 10.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (currentImageIndex == index) Color.White else Color.White.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }

                    // Miniaturas de imágenes
                    LazyRow(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed (serviceImages) { index, imageUrl ->
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (index == currentImageIndex) 3.dp else 0.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { currentImageIndex = index }
                            ) {
                                AsyncImage(
                                    model = imageUrl,            // ← aquí la URL de esta miniatura
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    // Degradado en la parte inferior
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0x99000000))
                                )
                            )
                    )
                }
            }

            // Información principal
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        detalle.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50))

                    Spacer(modifier = Modifier.height(15.dp))

                    // Rating y likes
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { i ->
                            val icon = when {
                                i < detalle.rating.toInt() -> Icons.Default.Star
                                i < detalle.rating             -> Icons.Default.StarHalf
                                else                           -> Icons.Default.StarBorder
                            }
                            Icon(icon, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(26.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Text("%.1f".format(detalle.rating), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(20.dp))
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isLiked) Color(0xFFFF5252) else Color(0xFF757575),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("$currentLikes me gusta", fontSize = 16.sp, color = Color(0xFF757575))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Detalles: duración y capacidad
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DetailItem(
                            Icons.Default.Schedule,
                            "Duración",
                            detalle.duracionServicio
                        )
                        DetailItem(
                            Icons.Default.People,
                            "Capacidad",
                            "${detalle.capacidadMaxima} personas"
                        )

                        DetailItem(
                            Icons.Default.LocationOn,
                            "Ubicación",
                            detalle.emprendimientoLocation)

                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    // Descripción
                    Text(
                        text = "Descripción",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = detalle.description.orEmpty(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color(0xFF455A64)
                        )
                    )
                }
            }

            // Emprendimiento
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ofrecido por:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color(0xFF757575)
                            ),
                            modifier = Modifier.padding(end = 16.dp)
                        )

                        AsyncImage(
                            model = detalle.emprendimientoImageUrl,
                            contentDescription = detalle.emprendimientoName,
                            modifier = Modifier
                                .size(55.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color(0xFF3498DB), CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = detalle.emprendimientoName,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verificado",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Comentarios
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp, bottom = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Comentarios (${comments.size})",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Ver todos",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color(0xFF2196F3),
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable { /* Ver todos los comentarios */ }
                    )
                }
            }

            // Lista de comentarios
            items(comments) { comment ->
                CommentItem(comment = comment)
                Spacer(modifier = Modifier.height(15.dp))
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun AlojamientoDetailItem(icon: ImageVector, title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(110.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE3F2FD))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        )

        Text(
            text = value,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AlojamientoCommentItem(comment: Comment) {
    var expanded by remember { mutableStateOf(false) }
    var liked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(comment.likes) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = comment.userImage),
                    contentDescription = comment.userName,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = comment.userName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Text(
                            text = comment.date,
                            color = Color(0xFFB0BEC5),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = comment.comment,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color(0xFF455A64)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Acciones del comentario
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón de like
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    liked = !liked
                                    likeCount = if (liked) comment.likes + 1 else comment.likes
                                }
                        ) {
                            Icon(
                                imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Me gusta",
                                tint = if (liked) Color(0xFFFF5252) else Color(0xFF90A4AE),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$likeCount",
                                color = if (liked) Color(0xFFFF5252) else Color(0xFF90A4AE),
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(25.dp))

                        // Botón de responder
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* Acción de responder */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Reply,
                                contentDescription = "Responder",
                                tint = Color(0xFF90A4AE),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Responder",
                                color = Color(0xFF90A4AE),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Respuestas
            if (comment.replies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(15.dp))

                // Botón para expandir/colapsar respuestas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(1.dp)
                            .background(Color(0xFFB0BEC5))
                    )

                    Text(
                        text = if (expanded) "Ocultar respuestas" else "Ver ${comment.replies.size} respuestas",
                        color = Color(0xFF2196F3),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Color(0xFFB0BEC5))
                    )
                }

                if (expanded) {
                    comment.replies.forEach { reply ->
                        ReplyItem(reply = reply)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AlojamientoReplyItem(reply: Reply) {
    var liked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(reply.likes) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp)
    ) {
        // Línea vertical
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(Color(0xFFE0E0E0))
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = reply.userImage),
                    contentDescription = reply.userName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = reply.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = reply.date,
                        color = Color(0xFFB0BEC5),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reply.comment,
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF455A64)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Acciones de respuesta
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón de like
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            liked = !liked
                            likeCount = if (liked) reply.likes + 1 else reply.likes
                        }
                ) {
                    Icon(
                        imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Me gusta",
                        tint = if (liked) Color(0xFFFF5252) else Color(0xFF90A4AE),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "$likeCount",
                        color = if (liked) Color(0xFFFF5252) else Color(0xFF90A4AE),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}