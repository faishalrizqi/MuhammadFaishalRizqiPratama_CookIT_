package com.muhammadfaishalrizqipratama0094.cookit_.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Add : Screen("add_screen")
    object Edit : Screen("edit_screen/{resepId}") {
        fun createRoute(resepId: String) = "edit_screen/$resepId"
    }
}