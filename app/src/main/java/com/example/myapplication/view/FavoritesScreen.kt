package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.model.database.FavoritePokemon
import com.example.myapplication.viewmodel.DetailViewModel

@Composable
fun FavoritesScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onPokemonClick: (Int) -> Unit
) {
    val favoritePokemon by viewModel.getAllFavorites().collectAsState(initial = emptyList())
    val listState = rememberLazyGridState()


    if (favoritePokemon.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Empty",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.LightGray,
                fontWeight = FontWeight.SemiBold
            )
        }
    } else {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(favoritePokemon) { pokemon ->
                FavoritePokemonItem(
                    pokemon = pokemon,
                    onClick = { onPokemonClick(pokemon.id) }
                )
            }
        }
    }

}


@Composable
fun FavoritePokemonItem(pokemon: FavoritePokemon, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 8.dp)
            .clickable { onClick() },

        colors = CardDefaults.cardColors(containerColor = Color.Yellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(pokemon.imageUrl),
                contentDescription = pokemon.name,
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pokemon.name,
                textAlign = TextAlign.Center
            )
        }
    }
}

