package com.example.myapplication.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: FavoritePokemon)

    @Delete
    suspend fun delete(pokemon: FavoritePokemon)

    @Query("SELECT * FROM favorite_pokemons")
    fun getAll(): Flow<List<FavoritePokemon>>

    @Query("SELECT * FROM favorite_pokemons WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<FavoritePokemon?>
}
