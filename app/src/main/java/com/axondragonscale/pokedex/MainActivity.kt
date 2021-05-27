package com.axondragonscale.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.axondragonscale.pokedex.ui.Route
import com.axondragonscale.pokedex.ui.screen.pokemondetail.PokemonDetailScreen
import com.axondragonscale.pokedex.ui.screen.pokemonlist.PokemonListScreen
import com.axondragonscale.pokedex.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeDexTheme {
                val navController = rememberNavController()
                NavHost(navController, Route.PokemonListScreen) {

                    composable(Route.PokemonListScreen) {
                        PokemonListScreen(navController)
                    }

                    composable(
                        route = "${Route.PokemonDetailScreen}/{${Route.DominantColorArg}}/{${Route.PokemonNameArg}}",
                        arguments = listOf(
                            navArgument(Route.DominantColorArg) { type = NavType.IntType },
                            navArgument(Route.PokemonNameArg) { type = NavType.StringType }
                        )
                    ) {
                        val dominantColor = remember {
                            val colorInt = it.arguments?.getInt(Route.DominantColorArg)
                            colorInt?.let { Color(it) } ?: Color.White
                        }

                        val pokemonName = remember { it.arguments?.getString(Route.PokemonNameArg) }

                        PokemonDetailScreen(
                            dominantColor = dominantColor,
                            pokemonName = pokemonName ?: "",
                            navController = navController
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokeDexTheme {

    }
}