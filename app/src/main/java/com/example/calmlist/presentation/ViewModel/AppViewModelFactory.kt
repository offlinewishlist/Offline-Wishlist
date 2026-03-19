package com.example.calmlist.presentation.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calmlist.AppContainer

class AppViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(
                loginUseCase = container.logInUser,
                createUserUseCase = container.createUserUseCase
            ) as T
        } else if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            return SyncViewModel(
                repository = container.wishRepository
            ) as T
        } else if (modelClass.isAssignableFrom(WishDetailViewModel::class.java)) {
            return WishDetailViewModel(
                repository = container.wishRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}