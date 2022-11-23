package com.arbonik.exchangerates.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arbonik.exchangerates.LoadState
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangesScreen(
    viewModel: MainViewModel = viewModel()
) {

    val exchanges = viewModel.exchanges.collectAsState()
//    val selectedExchange = viewModel.selectedExchange.collectAsState()
//    val exchangeCodes = viewModel.exchangeCodes.collectAsState(emptyList())

    val searchExchanges = viewModel.searchExchanges.collectAsState(emptyList())
    val enteredExchanges = viewModel.enteredExchange.collectAsState(
//        viewModel.enteredExchange.value
    )

    Column {
        OutlinedTextField(
            value = enteredExchanges.value,
            onValueChange = { text: String ->
                viewModel.enterExchange(text)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Выберите валюту")
            },
            singleLine = true
        )
        DropdownMenu(
            expanded = searchExchanges.value.isNotEmpty(),
            onDismissRequest = {
                viewModel.stopEnter()
            },
            modifier = Modifier.fillMaxWidth(),
            //properties = PopupProperties(focusable = false)
        ) {
            searchExchanges.value.forEach { label ->
                DropdownMenuItem(onClick = {
                    viewModel.pickExchange(label)
                }, text = {
                    Text(text = label)
                })
            }
        }

        when (exchanges.value) {
            is LoadState.Success -> {
                LazyColumn {
                    items(
                        (exchanges.value as? LoadState.Success<List<Exchange>>)?.data ?: emptyList()
                    ) {
                        Text(it.toString())
                    }
                }
            }

            is LoadState.Error -> {
                Text(text = "Ошибка загрузки данных")
            }

            LoadState.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}