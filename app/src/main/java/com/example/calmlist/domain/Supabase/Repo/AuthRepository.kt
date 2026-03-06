package com.example.calmlist.domain.supabase.repo

import com.example.calmlist.domain.supabase.SupabaseClient
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.UserData
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository {

    private val auth get() = SupabaseClient.client.auth

    fun loginUser(userData: UserData): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        val email = userData.email
        val password = userData.password

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            emit(ResultState.error("Email or password cannot be empty"))
            return@flow
        }

        try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = auth.currentUserOrNull()?.id
                ?: throw Exception("User session not created")

            emit(ResultState.Succes(userId))

        } catch (e: Exception) {
            emit(ResultState.error(e.message ?: "Login failed"))
        }
    }

    fun createUser(userData: UserData): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        val email = userData.email
        val password = userData.password

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            emit(ResultState.error("Email or password cannot be empty"))
            return@flow
        }

        try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = auth.currentUserOrNull()?.id
                ?: throw Exception("User session not created")

            emit(ResultState.Succes(userId))

        } catch (e: Exception) {
            emit(ResultState.error(e.message ?: "Sign up failed"))
        }
    }

    fun signOut(): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)

        try {
            auth.signOut()
            emit(ResultState.Succes(Unit))
        } catch (e: Exception) {
            emit(ResultState.error(e.message ?: "Sign out failed"))
        }
    }
}
