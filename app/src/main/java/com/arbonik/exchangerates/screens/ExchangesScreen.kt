package com.arbonik.exchangerates.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arbonik.exchangerates.viewModels.MainViewModel

@Composable
fun ExchangesScreen(
    viewModel: MainViewModel = viewModel()
) {
    LaunchedEffect(true){
        viewModel.loadExchanges()
    }

    val exchanges = viewModel.exchanges.collectAsState()

    LazyColumn {
        items(exchanges.value) {
            Text(it.toString())
        }
    }
}