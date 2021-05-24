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