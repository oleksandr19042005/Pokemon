package com.example.myapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexApp() {
    val navController = rememberNavController()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokedex") }
            )
        },

        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                    IconButton(onClick = { navController.navigate("favorites") }) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                PokemonScreen(
                    onPokemonClick = { pokemonName ->
                        navController.navigate("details/fromApi/$pokemonName")
                    }
                )
            }
            composable("favorites") {
                FavoritesScreen(
                    onPokemonClick = { pokemonId ->
                        navController.navigate("details/fromDb/$pokemonId")
                    }
                )
            }
            composable(
                route = "details/{source}/{value}"
            ) { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: ""
                val value = backStackEntry.arguments?.getString("value") ?: ""
                DetailPokemonScreen(
                    source = source,
                    value = value
                )
            }
        }
    }
}
