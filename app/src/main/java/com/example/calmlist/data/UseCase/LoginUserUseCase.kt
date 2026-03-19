package com.example.calmlist.data.UseCase

import com.example.calmlist.domain.supabase.repo.AuthRepository
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.UserData
import kotlinx.coroutines.flow.Flow

class LoginUserUseCase(
    private val repository: AuthRepository
) {
    fun loginUser(userData: UserData): Flow<ResultState<String>> {
        return repository.loginUser(userData)
    }

    fun checkUserSession(): Flow<ResultState<String?>> {
        return repository.getUserid()
    }

}