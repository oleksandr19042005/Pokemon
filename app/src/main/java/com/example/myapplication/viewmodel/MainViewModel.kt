package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ApiService
import com.example.myapplication.model.PokemonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val api: ApiService) : ViewModel() {

    private val _listOfPokemon = MutableLiveData<PokemonResponse>()
    val listOfPokemon: LiveData<PokemonResponse> = _listOfPokemon
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var currentOffset = 0
    private val pageSize = 30


    fun getListOfPokemon() {
        if (isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentList = _listOfPokemon.value?.results?.toMutableList() ?: mutableListOf()
                val response = withContext(Dispatchers.IO) {
                    api.getPokemonList(limit = pageSize, offset = currentOffset)
                }

                val updatedList = currentList + response.results
                _listOfPokemon.value = PokemonResponse(
                    count = response.count,
                    results = updatedList
                )

                currentOffset += pageSize

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
