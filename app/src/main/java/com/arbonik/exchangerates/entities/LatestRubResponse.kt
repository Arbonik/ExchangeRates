package com.arbonik.exchangerates.entities

import kotlinx.serialization.Serializable

@Serializable
data class LatestRubResponse(
    val base_code: String,
    val documentation: String,
    val provider: String,
    val rates: Map<String,Double>,
    val result: String,
    val terms_of_use: String,
    val time_eol_unix: Int,
    val time_last_update_unix: Int,
    val time_last_update_utc: String,
    val time_next_update_unix: Int,
    val time_next_update_utc: String
)