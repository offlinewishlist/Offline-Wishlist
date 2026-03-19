package com.example.calmlist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.calmlist.AppContainer
import com.example.calmlist.navigation.App
import com.example.calmlist.presentation.ViewModel.AppViewModel
import com.example.calmlist.presentation.ViewModel.AppViewModelFactory
import com.example.calmlist.presentation.ViewModel.SyncViewModel
import com.example.calmlist.presentation.ViewModel.WishDetailViewModel
import com.example.calmlist.ui.theme.CalmListTheme

class MainActivity : ComponentActivity() {

    private val appContainer by lazy { AppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG_APP", "MainActivity onCreate started")
        
        try {


            setContent {
                Log.d("DEBUG_APP", "MainActivity setContent started")
                    CalmListTheme(darkTheme = false, dynamicColor = false) {
                       androidx.compose.material3.Surface(
                           modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                           color = androidx.compose.ui.graphics.Color.White
                       ) {
                            val navController = rememberNavController()


                            val factory = AppViewModelFactory(appContainer)
                            val appViewModel: AppViewModel = viewModel(factory = factory)
                            val syncViewModel: SyncViewModel = viewModel(factory = factory)
                            val wishDetailViewModel: WishDetailViewModel = viewModel(factory = factory)

                            Scaffold {paddingValues ->
                                App(
                                    navController = navController,
                                    viewModel = appViewModel,
                                    syncViewModel = syncViewModel,
                                    wishDetailViewModel = wishDetailViewModel,
                                    paddingValues = paddingValues
                                )
                            }
                       }
                    }
            }
        } catch (e: Exception) {
            Log.e("DEBUG_APP", "CRASH DETECTED IN ONCREATE", e)
        }
    }
}
