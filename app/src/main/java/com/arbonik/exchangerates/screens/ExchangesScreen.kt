package com.arbonik.exchangerates.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.arbonik.exchangerates.LoadState
import com.arbonik.exchangerates.entities.Exchange
import com.arbonik.exchangerates.viewModels.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangesScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController
) {

    val exchanges = viewModel.exchanges.collectAsState()
    val loadState = viewModel.loadState.collectAsState()

    val searchExchanges = viewModel.searchExchanges.collectAsState(emptyList())
    val enteredExchanges = viewModel.enteredExchange.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                actions = {
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
                            properties = PopupProperties(focusable = false)
                        ) {
                            searchExchanges.value.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    viewModel.pickExchange(label)
                                }, text = {
                                    Text(text = label)
                                })
                            }
                        }
                    }
                },
                title = {

                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding()
                    )
            ) {
                when (loadState.value) {
                    LoadState.Success -> {
                        LazyColumn {
                            items(
                                exchanges.value
                            ) { exchange ->
                                ExchangeItem(exchange) { isFavorite: Boolean ->
                                    viewModel.setFavorite(exchange, isFavorite)
                                }
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
        },
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null
                    )
                }
                Spacer(Modifier.weight(1f, true))
                IconButton(onClick = {
                    navController.navigate("favorites")
                }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null
                    )
                }
            }
        },
    )
}

@Composable
fun ExchangeItem(
    exchange: Exchange,
    onFavoriteClicked: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onFavoriteClicked(exchange.isFavorite.not())
        }
    ) {
        Text(exchange.name + " " + exchange.cost + " " + exchange.isFavorite)
        if (exchange.isFavorite)
            Icon(imageVector = Icons.Filled.Favorite, contentDescription = "")
        else
            Icon(imageVector = Icons.Outlined.Star, contentDescription = "")
    }
}