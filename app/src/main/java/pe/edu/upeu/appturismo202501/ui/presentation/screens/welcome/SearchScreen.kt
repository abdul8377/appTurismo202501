package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable

private fun TopSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(Modifier.height(20.dp))
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 42.dp)
            .shadow(4.dp, RoundedCornerShape(24.dp)),
        placeholder = {
            Text("Encuentra lugares y actividades") },
        leadingIcon = {
            IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpiar")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor  = Color.White,
            focusedIndicatorColor   = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
            disabledIndicatorColor  = Color.Transparent,
            cursorColor             = MaterialTheme.colorScheme.primary
        )
    )
    Spacer(modifier = Modifier.height(6.dp))
    Divider()
}

@Composable
private fun RecentRow(text: String, onRemove: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF2F2F2)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(12.dp))
        Text(text, modifier = Modifier.weight(1f))
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = "borrar")
        }
    }

}

@Composable
private fun SuggestionRow(text: String, sub: String? = null, useLocation: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF2F2F2)),
            contentAlignment = Alignment.Center
        ) {
            if (useLocation) {
                Icon(Icons.Default.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            } else {
                Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text, fontWeight = FontWeight.Medium)
            sub?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
        }
    }

}

@Composable
fun SearchScreen(
    navController: NavController,
    suggestions: List<String>,
    recent: MutableList<String> = mutableStateListOf("Barcelona")
) {
    var query by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TopSearchBar(
            query = query,
            onQueryChange = { query = it },
            onBack = { navController.popBackStack() }
        )

        Spacer(Modifier.height(24.dp))

        // Búsquedas recientes header
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Búsquedas recientes", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { recent.clear() }) {
                Text("Borrar")
            }
        }
        Column(Modifier.verticalScroll(rememberScrollState())) {
            recent.forEach { item ->
                RecentRow(
                    text = item,
                    onRemove = { recent.remove(item) },
                    onClick = { query = item }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Sugerencias", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Column(Modifier.verticalScroll(rememberScrollState())) {
            SuggestionRow("Usar ubicación actual", useLocation = true, onClick = {})
            suggestions.forEach { city ->
                SuggestionRow(city, sub = "Ciudad en Francia", onClick = { query = city })
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        navController = rememberNavController(),
        suggestions = listOf("París", "Roma", "Barcelona")
    )
}