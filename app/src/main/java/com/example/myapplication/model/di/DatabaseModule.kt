package com.example.myapplication.model.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.model.database.PokemonDao
import com.example.myapplication.model.database.DataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java,
            "favorite_pokemons_db"
        ).build()
    }

    @Provides
    fun provideDao(db: DataBase): PokemonDao {
        return db.dao()
    }
}
