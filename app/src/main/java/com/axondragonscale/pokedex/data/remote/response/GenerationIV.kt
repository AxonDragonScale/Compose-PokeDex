package com.axondragonscale.pokedex.data.remote.response

import com.google.gson.annotations.SerializedName

data class GenerationIV(
    @SerializedName("diamond-pearl")
    val diamondPearl: DiamondPearl,
    @SerializedName("heartgold-soulsilver")
    val heartgoldSoulsilver: HeartgoldSoulsilver,
    val platinum: Platinum
)