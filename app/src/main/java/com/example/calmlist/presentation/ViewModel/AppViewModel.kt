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

    val userId = mutableStateOf<String?>(null)

    fun storeUserId(id: String) {
        userId.value = id
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