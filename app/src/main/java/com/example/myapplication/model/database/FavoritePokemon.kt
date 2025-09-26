package com.example.myapplication.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemons")
data class FavoritePokemon(
    @PrimaryKey() val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val imageUrl: String
)
