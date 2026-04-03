package com.example.calmlist.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calmlist.presentation.Screens.AddWishScreen
import com.example.calmlist.presentation.Screens.HomeScreen
import com.example.calmlist.presentation.Screens.LogInScreen
import com.example.calmlist.presentation.Screens.SettingsScreen
import com.example.calmlist.presentation.Screens.SignUpScreen
import com.example.calmlist.presentation.Screens.SplashScreen
import com.example.calmlist.presentation.Screens.WishDetailScreen
import com.example.calmlist.presentation.ViewModel.AppViewModel
import com.example.calmlist.presentation.ViewModel.SyncViewModel
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel

@Composable
fun App(navController: NavHostController, viewModel: AppViewModel, paddingValues: PaddingValues,syncViewModel: SyncViewModel,wishDetailViewModel: WishDetailViewModel){

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Routes.SplashScreen) {
            composable<Routes.SplashScreen> {
                SplashScreen(navController, viewModel)
            }
            composable<Routes.HomeScreen> {

                HomeScreen(navController,syncViewModel,wishDetailViewModel,viewModel)
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
            composable<Routes.AddWishScreen> {
                AddWishScreen(navController,viewModel,wishDetailViewModel)
            }
            composable<Routes.WishDetailScreen> {
                WishDetailScreen(navController,wishDetailViewModel)
            }

        }
    }

}