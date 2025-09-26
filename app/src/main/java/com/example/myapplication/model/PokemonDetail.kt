package com.example.myapplication.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val types: List<PokemonTypeSlot>?,
    val stats: List<PokemonStat>?
)

data class Sprites(val front_default: String?)
data class PokemonTypeSlot(val slot: Int, val type: NamedResource)
data class NamedResource(val name: String, val url: String)
data class PokemonStat(val base_stat: Int, val stat: NamedResource)
