package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ApiService
import com.example.myapplication.model.PokemonDetail
import com.example.myapplication.model.Sprites
import com.example.myapplication.model.database.FavoritePokemon
import com.example.myapplication.model.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val api: ApiService,
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> = _pokemonDetail

    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getPokemonFromApi(idOrNamePokemon: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getPokemonDetail(idOrNamePokemon)
                }
                _pokemonDetail.value = response

                checkIfFavorite(response.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getAllFavorites() = repository.getAllFavorites()
    fun getPokemonFromDb(id: Int) {
        viewModelScope.launch {
            val favorite = repository.getFavoriteById(id).firstOrNull()
            favorite?.let {
                _pokemonDetail.value = PokemonDetail(
                    id = it.id,
                    name = it.name,
                    height = it.height,
                    weight = it.weight,
                    sprites = Sprites(front_default = it.imageUrl),
                    types = null,
                    stats = null
                )
                _isFavorite.value = true
            } ?: run {
                _isFavorite.value = false
            }
        }
    }

    fun insertFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            repository.insertFavorite(pokemon)
            _isFavorite.postValue(true)
        }
    }

    fun deleteFavorite(pokemon: FavoritePokemon) {
        viewModelScope.launch {
            repository.deleteFavorite(pokemon)
            _isFavorite.postValue(false)
        }
    }

    private fun checkIfFavorite(id: Int) {
        viewModelScope.launch {
            val favorite = repository.getFavoriteById(id).firstOrNull()
            _isFavorite.postValue(favorite != null)
        }
    }
}