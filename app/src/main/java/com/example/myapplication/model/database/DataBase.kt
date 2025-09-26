package com.example.myapplication.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoritePokemon::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun dao(): PokemonDao

}