package com.axondragonscale.pokedex.ui.screen.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import coil.imageLoader
import coil.request.ImageRequest
import com.axondragonscale.pokedex.R
import com.axondragonscale.pokedex.data.models.PokedexEntry
import com.axondragonscale.pokedex.ui.Route
import com.axondragonscale.pokedex.ui.theme.RobotoCondensed
import com.axondragonscale.pokedex.util.Constants.PAGE_SIZE
import com.google.accompanist.coil.CoilImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Ronak Harkhani on 25/05/21
 */
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltNavGraphViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )

            Searchbar(modifier = Modifier.fillMaxWidth().padding(16.dp), hint = "Search...") {
                viewModel.searchPokemon(it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            PokedexList(navController = navController)
        }
    }
}

@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var showHint by remember { mutableStateOf(hint.isNotBlank()) }

    Box(modifier = modifier) {

        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
                .shadow(4.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .onFocusChanged {
                    showHint = !it.isFocused && text.isNotEmpty()
                }
        )

        if (showHint) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun PokedexList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltNavGraphViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val lastPageReached by remember { viewModel.lastPageReached }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = pokemonList.size / 2 + if (pokemonList.size % 2 == 0) 0 else 1
        items(itemCount) {
            if (it + PAGE_SIZE >= itemCount - 1 && !isLoading && !lastPageReached && !isSearching)
                viewModel.loadPokemonListPaginated()
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotBlank()) {
            RetrySection(loadError) {
                viewModel.loadPokemonListPaginated()
            }
        }
    }
}

@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexEntry>,
    navController: NavController
) {
    Column {

        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size > (rowIndex * 2) + 1) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PokedexEntry(
    entry: PokedexEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltNavGraphViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .aspectRatio(1f)
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
            .clickable {
                navController.navigate(
                    "${Route.PokemonDetailScreen}/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }
    ) {
        Column {

            CoilImage(
                request = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .target {
                        viewModel.calculateDominantColor(it) { color ->
                            dominantColor = color
                        }
                    }
                    .build(),
                contentDescription = entry.pokemonName,
                fadeIn = true,
                modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally)
            ) {
                // This gets shown while the image is loading
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.25f)
                )
            }

            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RetrySection(error: String, onRetry: () -> Unit) {
    Column {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onRetry() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}