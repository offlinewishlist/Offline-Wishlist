package com.example.calmlist.data.UseCase


import com.example.calmlist.domain.supabase.repo.AuthRepository
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.UserData
import kotlinx.coroutines.flow.Flow

class CreateUserUseCase(
    private val repository: AuthRepository
) {
    fun createUser(userData: UserData): Flow<ResultState<String>> {
        return repository.createUser(userData)
    }

}