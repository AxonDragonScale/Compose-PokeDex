package com.axondragonscale.pokedex.data.remote.response

import com.google.gson.annotations.SerializedName

data class GenerationII(
    val crystal: Crystal,
    val gold: Gold,
    val silver: Silver
)