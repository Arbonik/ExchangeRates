package com.arbonik.exchangerates.entities

import kotlinx.serialization.Serializable

@Serializable
data class Exchange(
    val name : String,
    val cost : Double
)
