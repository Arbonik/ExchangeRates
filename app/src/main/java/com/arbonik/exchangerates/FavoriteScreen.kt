package com.arbonik.exchangerates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.entities.FavExchangePairWithCost
import com.arbonik.exchangerates.entities.FavoriteExchangePair
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    navController: NavController
) {
    val data = viewModel.exchangeWithCost.collectAsState()

    LazyColumn {
        items(data.value) { pair ->
            ExchangeFavoriteItem(pair) {exchangePair ->
                viewModel.deleteFavorite(exchangePair)
            }
        }
    }
}

@Composable
fun ExchangeFavoriteItem(
    exchange: StateFlow<ExchangeLoadingState>,
    onFavoriteClicked: (FavoriteExchangePair) -> Unit
) {
    val exchangeState = exchange.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = exchangeState.value.exchangePair.first)
        Text(text = " - ")
        Text(text = exchangeState.value.exchangePair.second)
        Text(text = " - ")
        if (exchangeState.value is ExchangeLoadingState.FromNetwork) {
            Text(text = (exchangeState.value as ExchangeLoadingState.FromNetwork).exchangePairWithCost.cost.toString())
        } else {
            CircularProgressIndicator()
        }
        IconButton(onClick = {
            onFavoriteClicked(
                exchange.value.exchangePair
            )
        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}