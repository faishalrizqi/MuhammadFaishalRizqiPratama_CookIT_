package com.muhammadfaishalrizqipratama0094.cookit_

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.muhammadfaishalrizqipratama0094.cookit_.navigation.Screen
import com.muhammadfaishalrizqipratama0094.cookit_.ui.screen.AddResepScreen
import com.muhammadfaishalrizqipratama0094.cookit_.ui.screen.EditResepScreen
import com.muhammadfaishalrizqipratama0094.cookit_.ui.screen.MainScreen
import com.muhammadfaishalrizqipratama0094.cookit_.ui.screen.MainViewModel
import com.muhammadfaishalrizqipratama0094.cookit_.ui.theme.CookIT_Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookIT_Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: MainViewModel = viewModel()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Main.route
                    ) {
                        composable(Screen.Main.route) {
                            MainScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Screen.Add.route) {
                            AddResepScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(
                            route = Screen.Edit.route,
                            arguments = listOf(navArgument("resepId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val resepId = backStackEntry.arguments?.getString("resepId")
                            requireNotNull(resepId) { "resepId parameter wasn't found." }
                            EditResepScreen(
                                navController = navController,
                                viewModel = viewModel,
                                resepId = resepId
                            )
                        }
                    }
                }
            }
        }
    }
}