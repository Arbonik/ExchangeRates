package com.arbonik.exchangerates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExchangeRatesTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = ExNavigation.MAIN){
                    composable(ExNavigation.MAIN){
                        val viewModel = hiltViewModel<MainViewModel>()
                        ExchangesScreen(
                            viewModel,
                            navController
                        )
                    }
                    composable(ExNavigation.FAVORITES){
                        val viewModel = hiltViewModel<FavoriteViewModel>()
                        FavoriteScreen(
                            viewModel,
                            navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SortedAlertDialog(

) {
//    AlertDialog(onDismissRequest = {  }) {
//
//    }
}

data class ExchangeFilter(
    val nameFilter : Filter = Filter.No,
    val valueFilter : Filter = Filter.No
)

enum class Filter{
    Ascending,
    Вescending,
    No
}
object ExNavigation {
    const val SORTED = "sorted"
    const val MAIN = "allExchanges"
    const val FAVORITES = "favorites"
}