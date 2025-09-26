package com.example.myapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.model.database.FavoritePokemon
import com.example.myapplication.viewmodel.DetailViewModel

@Composable
fun DetailPokemonScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    source: String,
    value: String
) {
    LaunchedEffect(source, value) {
        if (source == "fromDb") {
            viewModel.getPokemonFromDb(value.toInt())
        } else {
            viewModel.getPokemonFromApi(value)
        }
    }

    val detailOfPokemon by viewModel.pokemonDetail.observeAsState()
    val isFavorite by viewModel.isFavorite.observeAsState(false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (detailOfPokemon != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                detailOfPokemon?.name?.replaceFirstChar { it.uppercase() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                val imageUrl = detailOfPokemon?.sprites?.front_default ?: detailOfPokemon
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Pokemon sprite",
                        modifier = Modifier.size(200.dp)
                    )
                } else {
                    Text("No image available")
                }


                Text("Height: ${detailOfPokemon?.height ?: "N/A"}")
                Text("Weight: ${detailOfPokemon?.weight ?: "N/A"}")

                if (source == "fromApi") {
                    Text("Types:", fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        detailOfPokemon?.types?.forEach { typeSlot ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.LightGray)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = typeSlot.type.name)
                            }
                        }
                    }

                    Text("Base Stats:", fontWeight = FontWeight.Bold)
                    Column {
                        detailOfPokemon?.stats?.forEach { stat ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(stat.stat.name.replaceFirstChar { it.uppercase() })
                                Text(stat.base_stat.toString())
                            }
                        }
                    }


                }
                IconButton(
                    onClick = {
                        detailOfPokemon?.let { pokemon ->
                            val favoritePokemon = FavoritePokemon(
                                id = pokemon.id,
                                name = pokemon.name ?: "Unknown",
                                weight = pokemon.weight ?: 0,
                                height = pokemon.height ?: 0,
                                imageUrl = pokemon.sprites?.front_default ?: ""
                            )
                            if (isFavorite) {
                                viewModel.deleteFavorite(favoritePokemon)
                            } else {
                                viewModel.insertFavorite(favoritePokemon)
                            }
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}
