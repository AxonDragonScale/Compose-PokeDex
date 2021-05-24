package com.axondragonscale.pokedex.repository

import com.axondragonscale.pokedex.data.remote.PokeApi
import com.axondragonscale.pokedex.data.remote.response.Pokemon
import com.axondragonscale.pokedex.data.remote.response.PokemonList
import com.axondragonscale.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 25/05/21
 */
@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An error occured.")
        }

        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("An error occured.")
        }

        return Resource.Success(response)
    }
}