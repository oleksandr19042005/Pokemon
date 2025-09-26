package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.model.PokemonResult
import com.example.myapplication.viewmodel.PokemonViewModel

@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onPokemonClick: (String) -> Unit
) {
    val listOfPokemon by viewModel.listOfPokemon.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val listState = rememberLazyGridState()


    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                if (lastVisible >= totalItems - 1 && !isLoading) {
                    viewModel.getListOfPokemon()
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.getListOfPokemon()
    }
    if (listOfPokemon!=null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Text(
                    text = "No Pokémon found ⚡",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.LightGray
                )
            }
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            listOfPokemon?.results?.let { pokemons ->
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(pokemons) { pokemon ->
                        PokemonItem(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.name) }
                        )
                    }

                    if (isLoading) {
                        item(span = { GridItemSpan(2) }) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(48.dp)
                            )
                        }
                    }
                }
            }

            if (listOfPokemon == null && isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }else{
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Failed to load data",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.LightGray,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun PokemonItem(pokemon: PokemonResult, onClick: () -> Unit) {
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

