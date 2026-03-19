package com.example.calmlist.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calmlist.R
import com.example.calmlist.navigation.Routes
import com.example.calmlist.presentation.ViewModel.AppViewModel
import kotlinx.coroutines.delay
import java.time.Month
//@Preview(showBackground = true)
@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: AppViewModel
) {
    val state = viewModel.logINScreenstate.value


    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LaunchedEffect(state.success, state.error) {
        when {
            state.success == true -> {
                navController.navigate(Routes.HomeScreen) {
                    popUpTo(Routes.SplashScreen) { inclusive = true }
                }
            }

            state.success == false -> {
                navController.navigate(Routes.LogINScreen) {
                    popUpTo(Routes.SplashScreen) { inclusive = true }
                }
            }
        }
    }


    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.serene_background)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Offline WishList",
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.serene_text_primary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Want it. Remember it. Decide later.",
                fontSize = 14.sp,
                color = colorResource(R.color.serene_text_secondary)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (state.isLoading) {
                CircularProgressIndicator(
                    color = colorResource(R.color.serene_primary),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}
