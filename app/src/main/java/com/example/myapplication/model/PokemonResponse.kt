package com.example.myapplication.model

data class PokemonResponse(
    val count: Int,
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
) {

    val imageUrl: String
        get() {
            val id = url.trimEnd('/').substringAfterLast("/")
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        }
}
