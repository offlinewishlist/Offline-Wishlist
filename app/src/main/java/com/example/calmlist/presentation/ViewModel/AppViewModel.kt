package com.example.calmlist.presentation.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calmlist.data.UseCase.CreateUserUseCase
import com.example.calmlist.data.UseCase.LoginUserUseCase
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.UserData
import kotlinx.coroutines.launch

import android.content.Context
import android.content.SharedPreferences
import com.example.calmlist.data.Repo.WishRepository
import com.example.calmlist.domain.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth

class AppViewModel(
    val loginUseCase: LoginUserUseCase,
    val createUserUseCase: CreateUserUseCase,
    private val wishRepository: WishRepository,
    private val context: Context
): ViewModel() {
    private val _logInScreenstate = mutableStateOf(LogINScreenSate())
    val logINScreenstate = _logInScreenstate

    private val _signupScreenstate = mutableStateOf(SignUpScreenSate())
    val signupScreenstate = _signupScreenstate

    val userId = mutableStateOf<String?>(null)
    
    // Settings State
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    
    val storageUsage = mutableStateOf("Calculating...")

    init {
        calculateStorageUsage()
    }

    fun storeUserId(id: String) {
        userId.value = id
    }
    

    
    fun calculateStorageUsage() {
        viewModelScope.launch {
             try {
                val filesDirSize = getFolderSize(context.filesDir)

                val dbSize = context.getDatabasePath("wish_database").length()
                
                val totalBytes = filesDirSize + dbSize
                storageUsage.value = formatSize(totalBytes)
            } catch (e: Exception) {
                storageUsage.value = "Unknown"
            }
        }
    }
    
    private fun getFolderSize(file: java.io.File): Long {
        if (!file.exists()) return 0
        if (!file.isDirectory) return file.length()
        var length: Long = 0
        val files = file.listFiles() ?: return 0
        for (f in files) {
            length += getFolderSize(f)
        }
        return length
    }
    
    private fun formatSize(bytes: Long): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        return when {
            mb >= 1 -> String.format("%.2f MB", mb)
            kb >= 1 -> String.format("%.2f KB", kb)
            else -> "$bytes Bytes"
        }
    }

    fun logout() {
        // Reset state immediately to prevent race condition with navigation
        userId.value = null
        _logInScreenstate.value = LogINScreenSate()
        
        viewModelScope.launch {
            try {
                SupabaseClient.client.auth.signOut()
                // Clear local data optional on logout? Request said "clears local cache".
                // I will clear local DB to ensure privacy.
                wishRepository.clearAllWishes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun clearLocalData() {
        viewModelScope.launch {
            wishRepository.clearAllWishes()
            calculateStorageUsage()
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            loginUseCase.checkUserSession().collect { result ->
                when (result) {
                    ResultState.Loading -> {
                        _logInScreenstate.value = LogINScreenSate(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        if (result.data != null) {
                            storeUserId(result.data)

                            _logInScreenstate.value = LogINScreenSate(
                                success = true,
                                userdata = result.data
                            )
                        } else {

                            _logInScreenstate.value = LogINScreenSate(
                                success = false,
                                error = null
                            )
                        }
                    }

                    is ResultState.error -> {
                        _logInScreenstate.value = LogINScreenSate(
                            error = result.message
                        )
                    }
                }
            }
        }
    }



    fun LoginUser(userdata: UserData) {
        viewModelScope.launch {
            loginUseCase.loginUser(userdata).collect { result ->
                when (result) {
                    ResultState.Loading -> LogINScreenSate(isLoading = true)
                    is ResultState.Succes -> {
                        if (result.data != null) {
                            storeUserId(result.data)
                        }
                        _logInScreenstate.value = LogINScreenSate(
                            success = true, userdata = result.data
                        )


                    }

                    is ResultState.error -> {
                        _logInScreenstate.value = LogINScreenSate(error = result.message)
                    }
                }


            }
        }
    }

    fun createUser(userdata: UserData) {
        viewModelScope.launch {
            createUserUseCase.createUser(userdata).collect { result ->
                when (result) {
                    ResultState.Loading -> {
                        _signupScreenstate.value = SignUpScreenSate(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        if (result.data != null) {
                            storeUserId(result.data)
                        }
                        _signupScreenstate.value = SignUpScreenSate(
                            success = true, userdata = result.data
                        )
                    }

                    is ResultState.error -> {
                        _signupScreenstate.value = SignUpScreenSate(error = result.message)
                    }
                }
            }
        }
    }

}
data class SignUpScreenSate(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean? = false
)

data class LogINScreenSate(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean? = false
)