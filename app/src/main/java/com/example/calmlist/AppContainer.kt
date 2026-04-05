package com.example.calmlist

import com.example.calmlist.data.UseCase.CreateUserUseCase

import com.example.calmlist.data.UseCase.LoginUserUseCase
import com.example.calmlist.domain.supabase.SupabaseClient
import com.example.calmlist.domain.supabase.repo.AuthRepository


class AppContainer(val context: android.content.Context) {

    init {
       SupabaseClient.initialize(context)
    }

    private val authRepository = AuthRepository()
    private val database = com.example.calmlist.data.local.Database.WishDatabase.getDatabase(context)
    
    val wishRepository: com.example.calmlist.data.Repo.WishRepository = com.example.calmlist.data.Repo.WishRepository(
        dao = database.wishDao(),
        remote = com.example.calmlist.data.remote.SupabaseWishService()
    )

    val logInUser = LoginUserUseCase(authRepository)
    val createUserUseCase = CreateUserUseCase(authRepository)
}