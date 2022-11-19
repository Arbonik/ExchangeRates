package com.arbonik.exchangerates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arbonik.exchangerates.screens.ExchangesScreen
import com.arbonik.exchangerates.ui.theme.ExchangeRatesTheme
import com.arbonik.exchangerates.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExchangeRatesTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "allExchanges"){
                    composable("allExchanges"){
                        val viewModel = hiltViewModel<MainViewModel>()
                        ExchangesScreen(
                            viewModel
                        )
                    }
                }

//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
            }
        }
    }
}