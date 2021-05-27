package com.axondragonscale.pokedex.ui.screen.pokemondetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.axondragonscale.pokedex.data.remote.response.Pokemon
import com.axondragonscale.pokedex.util.Resource
import com.google.accompanist.coil.CoilImage
import java.util.*

/**
 * Created by Ronak Harkhani on 28/05/21
 */
@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    imageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltNavGraphViewModel()
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName.toLowerCase(Locale.ROOT))
    }.value

    Box(modifier = Modifier.fillMaxSize().background(dominantColor).padding(bottom = 16.dp)) {

        PokemonDetailNavBar(
            navController = navController,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f).align(Alignment.TopCenter)
        )

        PokemonDetailStateWrapper(
            pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + imageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + imageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )

        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
            if (pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let {
                    CoilImage(
                        data = it.frontDefault,
                        contentDescription = pokemonInfo.data.name,
                        fadeIn = true,
                        modifier = Modifier.size(imageSize).offset(y = topPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailNavBar(navController: NavController, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(Color.Black, Color.Transparent)
            )
        )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable { navController.popBackStack() }
        )
    }
}

@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier,
    loadingModifier: Modifier
) {
    when (pokemonInfo) {
        is Resource.Success -> {

        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message ?: "An uknown error occured.",
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}