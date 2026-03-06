package com.example.calmlist

import com.example.calmlist.data.UseCase.CreateUserUseCase
import com.example.calmlist.data.UseCase.LoginUserUseCase
import com.example.calmlist.domain.supabase.repo.AuthRepository


class AppContainer {

    private val authRepository = AuthRepository()

    val logInUser = LoginUserUseCase(authRepository)
    val createUserUseCase = CreateUserUseCase(authRepository)
}