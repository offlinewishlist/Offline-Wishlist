package com.example.calmlist.presentation.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calmlist.data.UseCase.CreateUserUseCase
import com.example.calmlist.data.UseCase.LoginUserUseCase
import com.example.calmlist.model.ResultState
import com.example.calmlist.model.UserData
import kotlinx.coroutines.launch

class AppViewModel(val loginUseCase: LoginUserUseCase, val createUserUseCase: CreateUserUseCase): ViewModel() {
    private val _logInScreenstate = mutableStateOf(LogINScreenSate())
    val logINScreenstate = _logInScreenstate

    private val _signupScreenstate = mutableStateOf(SignUpScreenSate())
    val signupScreenstate = _signupScreenstate


    fun LoginUser(userdata: UserData) {
        viewModelScope.launch {
            loginUseCase.loginUser(userdata).collect { result ->
                when (result) {
                    ResultState.Loading -> LogINScreenSate(isLoading = true)
                    is ResultState.Succes -> {
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