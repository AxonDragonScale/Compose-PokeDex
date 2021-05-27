package com.axondragonscale.pokedex.ui.screen.pokemondetail

import androidx.lifecycle.ViewModel
import com.axondragonscale.pokedex.data.remote.response.Pokemon
import com.axondragonscale.pokedex.repository.PokemonRepository
import com.axondragonscale.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 28/05/21
 */
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }
}