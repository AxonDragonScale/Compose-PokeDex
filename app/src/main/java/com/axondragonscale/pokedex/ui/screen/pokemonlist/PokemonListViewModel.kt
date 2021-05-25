package com.axondragonscale.pokedex.ui.screen.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.axondragonscale.pokedex.data.models.PokedexEntry
import com.axondragonscale.pokedex.repository.PokemonRepository
import com.axondragonscale.pokedex.util.Constants.PAGE_SIZE
import com.axondragonscale.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Ronak Harkhani on 26/05/21
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0
    var pokemonList = mutableStateOf<List<PokedexEntry>>(listOf())
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var lastPageReached = mutableStateOf(false)

    init {
        loadPokemonListPaginated()
    }

    fun loadPokemonListPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    lastPageReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    val entries = result.data.results.mapIndexed { index, entry ->
                        val number = parseUrlForNumber(entry.url)
                        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"

                        PokedexEntry(entry.name.capitalize(Locale.ROOT), imageUrl, number.toInt())
                    }

                    pokemonList.value += entries
                    currentPage++
                    isLoading.value = false
                    loadError.value = ""
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    private fun parseUrlForNumber(url: String): String {
        return if (url.endsWith("/")) {
            url.dropLast(1).takeLastWhile { it.isDigit() }
        } else {
            url.takeLastWhile { it.isDigit() }
        }
    }

    /**
     * Takes a drawable and calculates the dominant color in it.
     */
    fun calculateDominantColor(drawable: Drawable, onFinished: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { color ->
                onFinished(Color(color))
            }
        }
    }
}