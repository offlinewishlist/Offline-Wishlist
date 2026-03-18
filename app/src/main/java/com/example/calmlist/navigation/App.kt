package com.example.calmlist.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calmlist.navigation.Routes
import com.example.calmlist.presentation.Screens.HomeScreen
import com.example.calmlist.presentation.Screens.LogInScreen
import com.example.calmlist.presentation.Screens.SettingsScreen
import com.example.calmlist.presentation.Screens.SignUpScreen
import com.example.calmlist.presentation.Screens.SplashScreen
import com.example.calmlist.presentation.ViewModel.AppViewModel
import com.example.calmlist.presentation.ViewModel.SyncViewModel
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel

@Composable
fun App(navController: NavHostController, viewModel: AppViewModel,syncViewModel: SyncViewModel,wishDetailViewModel: WishDetailViewModel, paddingValues: PaddingValues){
    android.util.Log.d("DEBUG_APP", "App composable rendered")
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Routes.LogINScreen) {
            composable<Routes.SplashScreen> {
                SplashScreen(navController)
            }
            composable<Routes.HomeScreen> {
                HomeScreen(navController = navController, wishViewModel = wishDetailViewModel, syncViewModel = syncViewModel)
            }
            composable<Routes.SettingsScreen> {
                SettingsScreen(navController)

            }
            composable<Routes.LogINScreen> {
                LogInScreen(viewModel, navController)

            }
            composable<Routes.SignUPScreen> {
                SignUpScreen(viewModel, navController)
            }

        }
    }

}