package com.axondragonscale.pokedex.data.remote.response

import com.google.gson.annotations.SerializedName

data class VersionGroup(
    val name: String,
    val url: String
)