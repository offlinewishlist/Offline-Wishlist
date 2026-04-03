package com.example.calmlist.navigation
import kotlinx.serialization.Serializable
sealed class Routes {
@Serializable
object SplashScreen: Routes()
    @Serializable
    object HomeScreen: Routes()
    @Serializable
    object SettingsScreen: Routes()
    @Serializable
    object WishListScreen: Routes()
    @Serializable
    object AddWishScreen: Routes()
    @Serializable
    object LogINScreen: Routes()
    @Serializable
    object SignUPScreen: Routes()
    @Serializable
    object WishDetailScreen: Routes()

}