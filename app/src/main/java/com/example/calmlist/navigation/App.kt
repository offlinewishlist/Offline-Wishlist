import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calmlist.navigation.Routes
import com.example.calmlist.presentation.Screens.HomeScreen
import com.example.calmlist.presentation.Screens.SettingsScreen
import com.example.calmlist.presentation.Screens.SplashScreen

@Composable
fun App(navController: NavHostController){
    NavHost(navController=navController, startDestination = Routes.SplashScreen){
        composable<Routes.SplashScreen>{
            SplashScreen(navController)
        }
        composable <Routes.HomeScreen>{
            HomeScreen(navController)
        }
        composable<Routes.SettingsScreen>{
            SettingsScreen(navController)

        }

    }

}