package com.example.myapplication.model.repository

import com.example.myapplication.model.database.FavoritePokemon
import com.example.myapplication.model.database.PokemonDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepository @Inject constructor(private val dao: PokemonDao) {

    fun getAllFavorites(): Flow<List<FavoritePokemon>> = dao.getAll()

    suspend fun insertFavorite(pokemon: FavoritePokemon) = dao.insert(pokemon)
    fun getFavoriteById(id: Int): Flow<FavoritePokemon?> = dao.getById(id)
    suspend fun deleteFavorite(pokemon: FavoritePokemon) = dao.delete(pokemon)

}