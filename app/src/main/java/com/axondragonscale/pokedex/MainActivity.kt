package com.axondragonscale.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.axondragonscale.pokedex.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val PokemonListScreen = "PokemonListScreen"

        const val PokemonDetailScreen = "PokemonDetailScreen"
        const val DominantColorArg = "DominantColorArg"
        const val PokemonNameArg = "PokemonNameArg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeDexTheme {
                val navController = rememberNavController()
                NavHost(navController, PokemonListScreen) {
                    composable(PokemonListScreen) { }
                    composable(
                        route = "$PokemonDetailScreen/{$DominantColorArg}/{$PokemonNameArg}",
                        arguments = listOf(
                            navArgument(DominantColorArg) { type = NavType.IntType },
                            navArgument(PokemonNameArg) { type = NavType.StringType }
                        )
                    ) {
                        val dominantColor = remember {
                            val colorInt = it.arguments?.getInt(DominantColorArg)
                            colorInt?.let { Color(it) } ?: Color.White
                        }

                        val pokemonName = remember { it.arguments?.getString(PokemonNameArg) }
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